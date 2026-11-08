package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerPositionAndRotationPacket;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.packet.PositionAndRotationPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerPositionAndRotationPacket
        extends LoaderPacketHandler<ClientPlayerPositionAndRotationPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientPlayerPositionAndRotationPacket packet) {
        return new PositionAndRotationPacket(
                DeyoPlayer.players.get(uuid),
                new Pos(packet.position().x(),
                        packet.position().y(),
                        packet.position().z(),
                        packet.position().yaw(),
                        packet.position().pitch()),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerPositionAndRotationPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return new ClientPlayerPositionAndRotationPacket(
                new net.minestom.server.coordinate.Pos(
                        ((PositionAndRotationPacket) packet).getPos().x(),
                        ((PositionAndRotationPacket) packet).getPos().y(),
                        ((PositionAndRotationPacket) packet).getPos().z(),
                        ((PositionAndRotationPacket) packet).getPos().yaw(),
                        ((PositionAndRotationPacket) packet).getPos().pitch()
                ),
                (byte) (((PositionAndRotationPacket) packet).isOnGround() ? 1 : 0)
        );
    }

    @Override
    public Class<ClientPlayerPositionAndRotationPacket> getHandledPacketClass() {
        return ClientPlayerPositionAndRotationPacket.class;
    }
}
