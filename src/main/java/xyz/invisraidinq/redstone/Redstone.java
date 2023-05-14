package xyz.invisraidinq.redstone;

import xyz.invisraidinq.redstone.packet.RedstonePacket;

public interface Redstone {

    void sendPacket(RedstonePacket redstonePacket);

    static RedstoneInitializer.Builder builder() {
        return new RedstoneInitializer.Builder();
    }
}
