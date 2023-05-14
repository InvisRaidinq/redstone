package example;

import example.packet.TestPacket;
import xyz.invisraidinq.redstone.Redstone;

public class ExampleMain {

    public static void main(String[] args) {
        final Redstone redstone = Redstone.builder()
            .address("127.0.0.1")
            .port(6379)
            .channel("test-redis")
            .build();

        redstone.sendPacket(new TestPacket("asdsadasd"));
    }
}
