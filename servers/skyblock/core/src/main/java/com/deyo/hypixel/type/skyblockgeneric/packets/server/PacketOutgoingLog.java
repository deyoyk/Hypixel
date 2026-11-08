package com.deyo.hypixel.type.skyblockgeneric.packets.server;

import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.configuration.FinishConfigurationPacket;
import com.deyo.hypixel.type.generic.packet.HypixelPacketServerListener;

public class PacketOutgoingLog extends HypixelPacketServerListener {
    @Override
    public Class<? extends ServerPacket> getPacket() {
        return FinishConfigurationPacket.class;
    }

    @Override
    public void run(ServerPacket packet) {

    }
}
