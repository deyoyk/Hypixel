package com.deyo.hypixel.type.skyblockgeneric.packets.client;

import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.ClientPacket;
import net.minestom.server.network.packet.client.play.ClientPlayerActionPacket;
import net.minestom.server.network.packet.server.play.AcknowledgeBlockChangePacket;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.packet.HypixelPacketClientListener;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerDamageSkyBlockBlockEvent;

public class PacketListenerPlayerDig extends HypixelPacketClientListener {
    @Override
    public Class<? extends ClientPacket> getPacket() {
        return ClientPlayerActionPacket.class;
    }

    @Override
    public void run(PlayerPacketEvent event, ClientPacket packet, HypixelPlayer player) {
        ClientPlayerActionPacket digPacket = (ClientPlayerActionPacket) packet;
        HypixelEventHandler.callCustomEvent(new PlayerDamageSkyBlockBlockEvent(
                player,
                digPacket.blockPosition(),
                digPacket.status(),
                digPacket.sequence()));
        player.getPlayerConnection().sendPacket(new AcknowledgeBlockChangePacket(digPacket.sequence()));
    }

    @Override
    public boolean overrideMinestomProcessing() {
        return false;
    }
}
