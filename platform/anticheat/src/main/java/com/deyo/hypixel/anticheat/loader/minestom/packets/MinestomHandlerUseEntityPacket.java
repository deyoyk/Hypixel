package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientInteractEntityPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.event.packet.UseEntityPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerUseEntityPacket
        extends LoaderPacketHandler<ClientInteractEntityPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientInteractEntityPacket packet) {
        return new UseEntityPacket(
                uuid,
                packet.targetId(),
            Pos.fromVec(packet.location()),
            packet.hand()
        );
    }

    @Override
    public ClientInteractEntityPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientInteractEntityPacket> getHandledPacketClass() {
        return ClientInteractEntityPacket.class;
    }
}
