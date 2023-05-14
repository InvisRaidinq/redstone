package xyz.invisraidinq.redstone.manager.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import xyz.invisraidinq.redstone.packet.RedstonePacket;

public class ConnectionManager {
    private final JedisPool jedisPool;
    private final String channel;
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static final Gson GSON = new GsonBuilder().create();
    private static final String CLASS_IDENTIFIER = "redstone-packet-class";
    private static final String DATA_IDENTIFIER = "redstone-packet-data";

    public ConnectionManager(JedisPool jedisPool, String channel) {
        this.jedisPool = jedisPool;
        this.channel = channel;

        this.initSubscription();
    }

    @SneakyThrows
    public void sendPacket(RedstonePacket redstonePacket) {
        final Map<String, Object> fieldMap = new HashMap<>();
        for (final Field field : redstonePacket.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(redstonePacket));
        }

        final JsonObject jsonObject = GSON.fromJson(GSON.toJson(fieldMap), JsonObject.class);
        jsonObject.addProperty(CLASS_IDENTIFIER, redstonePacket.getClass().getName());

        this.executorService.submit(() -> this.handle((jedis) -> jedis.publish(this.channel, jsonObject.toString())));
    }

    private void handle(Consumer<Jedis> consumer) {
        try (final Jedis jedis = this.jedisPool.getResource()) {
            if (jedis == null) {
                throw new IllegalStateException("Failed to handle Jedis event");
            }

            consumer.accept(jedis);
        }
    }

    private void initSubscription() {
        final Jedis jedis = this.jedisPool.getResource();

        // Handle messages on a separate messaging thread
        new Thread(() -> {
            final JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                @SneakyThrows
                public void onMessage(String channel, String message) {
                    if (!channel.equals(ConnectionManager.this.channel)) {
                        return;
                    }

                    final JsonObject jsonObject = GSON.fromJson(message, JsonObject.class);
                    final Class<?> packetClass = Class.forName(jsonObject.remove(CLASS_IDENTIFIER).getAsString());
                    final RedstonePacket redstonePacket = (RedstonePacket) GSON.fromJson(jsonObject, packetClass);

                    redstonePacket.onReceive();
                }
            };

            jedis.subscribe(jedisPubSub, this.channel);
        }).start();
    }

}
