package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.network.packet.client.play.ClientAnimationPacket;
import com.deyo.hypixel.anticheat.event.packet.AnimationPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerAnimationPacket
        extends LoaderPacketHandler<ClientAnimationPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientAnimationPacket packet) {
        AnimationPacket.Hand hand = packet.hand() == PlayerHand.MAIN ?
                AnimationPacket.Hand.MAIN_HAND : AnimationPacket.Hand.OFF_HAND;

        return new AnimationPacket(uuid, hand);
    }

    @Override
    public ClientAnimationPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientAnimationPacket> getHandledPacketClass() {
        return ClientAnimationPacket.class;
    }
}
