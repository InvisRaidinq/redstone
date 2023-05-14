package example.packet;

import xyz.invisraidinq.redstone.packet.RedstonePacket;

public class TestPacket implements RedstonePacket {
    private final String test;

    public TestPacket(String test) {
        this.test = test;
    }

    @Override
    public void onReceive() {

    }
}
