package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.common.ClientPongPacket;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.packet.PingResponsePacket;
import com.deyo.hypixel.anticheat.event.packet.RequestPingPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPingResponsePacket
        extends LoaderPacketHandler<ClientPongPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientPongPacket packet) {
        return new PingResponsePacket(
                DeyoPlayer.players.get(uuid),
                packet.id()
        );
    }

    @Override
    public ClientPongPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return new ClientPongPacket(
                ((RequestPingPacket) packet).getRequestId()
        );
    }

    @Override
    public Class<ClientPongPacket> getHandledPacketClass() {
        return ClientPongPacket.class;
    }
}
