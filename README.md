
# 🔴 Redstone
**Redstone** is a [Redis](https://redis.io) pub-sub implementation using [Jedis](https://github.com/redis/jedis) which enables easy and effective handling of data.
## Usage
An example implementation can be found [here](https://github.com/InvisRaidinq/redstone/tree/main/src/main/java/example) if you don't want a step-by-step walkthrough.

Implementing **Redstone** is incredibly easy. First you'll need to obtain an instance of the `Redstone` interface:

```java
final Redstone redstone = Redstone.builder()
    .address("127.0.0.1")
    .port(6379)
    .channel("redis-channel-here")
    .build();                    
```

Packets can be published with the `redstone#sendPacket` method.

### Creating your first packet
A packet is simply a class that implements `RedstonePacket`:

```java
public class SomePacket implements RedstonePacket {
    private final String someString;
    private final UUID someUuid;
    private final double someDouble;

    public SomePacket(String someDouble, UUID someUuid, double someDouble) {
        this.someString = someString;
        this.someUuid = someUuid;
        this.someDouble = someDouble;
    }

    @Override
    public void onReceive() {
        // Handle receiving of this packet here
    }
}
```
When receiving a packet, it will automatically be deserialzied using the Google Gson library, meaning you might have to add support for non-supported objects.


## License

This project uses the [MIT](https://choosealicense.com/licenses/mit/) license, meaning you're pretty much free to do whatever with it!

