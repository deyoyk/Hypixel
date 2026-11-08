package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientInputPacket;
import com.deyo.hypixel.anticheat.event.packet.SteerVehiclePacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerSteerVehiclePacket
        extends LoaderPacketHandler<ClientInputPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientInputPacket packet) {

        float sideways = 0f;
        if (packet.left()) sideways -= 1f;
        if (packet.right()) sideways += 1f;

        float forward = 0f;
        if (packet.forward()) forward += 1f;
        if (packet.backward()) forward -= 1f;

        boolean jump = packet.jump();
        boolean unmount = packet.shift();

        return new SteerVehiclePacket(uuid, sideways, forward, jump, unmount);
    }

    @Override
    public ClientInputPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientInputPacket> getHandledPacketClass() {
        return ClientInputPacket.class;
    }
}