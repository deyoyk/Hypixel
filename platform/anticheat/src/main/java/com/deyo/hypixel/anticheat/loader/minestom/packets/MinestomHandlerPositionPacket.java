package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPositionPacket;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.packet.PositionPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerPositionPacket
        extends LoaderPacketHandler<ClientPlayerPositionPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientPlayerPositionPacket packet) {
        return new PositionPacket(
                DeyoPlayer.players.get(uuid),
                packet.position().x(),
                packet.position().y(),
                packet.position().z(),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerPositionPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return new ClientPlayerPositionPacket(
                new net.minestom.server.coordinate.Pos(
                        ((PositionPacket) packet).getX(),
                        ((PositionPacket) packet).getY(),
                        ((PositionPacket) packet).getZ()
                ),
                (byte) (((PositionPacket) packet).isOnGround() ? 1 : 0)
        );
    }

    @Override
    public Class<ClientPlayerPositionPacket> getHandledPacketClass() {
        return ClientPlayerPositionPacket.class;
    }
}
