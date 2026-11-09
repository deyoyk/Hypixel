package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.event.packet.KnockbackPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;
import com.deyo.hypixel.anticheat.math.Vel;

import java.util.UUID;

public class MinestomHandlerKnockbackPacket extends LoaderPacketHandler<EntityVelocityPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, EntityVelocityPacket packet) {
        Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null || packet.entityId() != player.getEntityId()) return null;

        Vec velocity = packet.velocity();
        return new KnockbackPacket(uuid, new Vel(velocity.x(), velocity.y(), velocity.z()));
    }

    @Override
    public EntityVelocityPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<EntityVelocityPacket> getHandledPacketClass() {
        return EntityVelocityPacket.class;
    }
}
