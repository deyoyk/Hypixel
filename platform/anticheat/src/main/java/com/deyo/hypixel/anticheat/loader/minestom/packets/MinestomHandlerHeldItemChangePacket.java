package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientHeldItemChangePacket;
import com.deyo.hypixel.anticheat.event.packet.HeldItemChangePacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerHeldItemChangePacket
        extends LoaderPacketHandler<ClientHeldItemChangePacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientHeldItemChangePacket packet) {
        return new HeldItemChangePacket(uuid, packet.slot());
    }

    @Override
    public ClientHeldItemChangePacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientHeldItemChangePacket> getHandledPacketClass() {
        return ClientHeldItemChangePacket.class;
    }
}
