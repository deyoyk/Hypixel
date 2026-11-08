package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientPlayerRotationPacket;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.packet.RotationPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerRotationPacket
        extends LoaderPacketHandler<ClientPlayerRotationPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientPlayerRotationPacket packet) {
        return new RotationPacket(
                DeyoPlayer.players.get(uuid),
                packet.yaw(),
                packet.pitch(),
                packet.onGround()
        );
    }

    @Override
    public ClientPlayerRotationPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return new ClientPlayerRotationPacket(
                ((RotationPacket) packet).getYaw(),
                ((RotationPacket) packet).getPitch(),
                (byte) (((RotationPacket) packet).isOnGround() ? 1 : 0)
        );
    }

    @Override
    public Class<ClientPlayerRotationPacket> getHandledPacketClass() {
        return ClientPlayerRotationPacket.class;
    }
}
