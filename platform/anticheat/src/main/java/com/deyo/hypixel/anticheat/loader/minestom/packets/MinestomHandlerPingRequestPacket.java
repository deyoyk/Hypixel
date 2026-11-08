package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.server.common.PingPacket;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.packet.RequestPingPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPingRequestPacket
        extends LoaderPacketHandler<PingPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, PingPacket packet) {
        return new RequestPingPacket(
                DeyoPlayer.players.get(uuid),
                packet.id()
        );
    }

    @Override
    public PingPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return new PingPacket(
                ((RequestPingPacket) packet).getRequestId()
        );
    }

    @Override
    public Class<PingPacket> getHandledPacketClass() {
        return PingPacket.class;
    }
}
