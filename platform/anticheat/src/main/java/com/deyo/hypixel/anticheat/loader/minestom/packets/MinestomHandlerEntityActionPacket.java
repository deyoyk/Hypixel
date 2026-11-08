package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.network.packet.client.play.ClientEntityActionPacket;
import com.deyo.hypixel.anticheat.event.packet.EntityActionPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;

import java.util.UUID;

public class MinestomHandlerEntityActionPacket
        extends LoaderPacketHandler<ClientEntityActionPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientEntityActionPacket packet) {
        EntityActionPacket.Action action = mapAction(packet.action());
        int jumpBoost = 0;

        if (action == EntityActionPacket.Action.START_JUMP_WITH_HORSE) {
            jumpBoost = 100;
        }

        return new EntityActionPacket(uuid, action, jumpBoost);
    }

    @Override
    public ClientEntityActionPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientEntityActionPacket> getHandledPacketClass() {
        return ClientEntityActionPacket.class;
    }

    private EntityActionPacket.Action mapAction(ClientEntityActionPacket.Action action) {
        return switch (action) {
            case LEAVE_BED -> EntityActionPacket.Action.LEAVE_BED;
            case START_SPRINTING -> EntityActionPacket.Action.START_SPRINTING;
            case STOP_SPRINTING -> EntityActionPacket.Action.STOP_SPRINTING;
            case START_JUMP_HORSE -> EntityActionPacket.Action.START_JUMP_WITH_HORSE;
            case STOP_JUMP_HORSE -> EntityActionPacket.Action.STOP_JUMP_WITH_HORSE;
            case OPEN_HORSE_INVENTORY -> EntityActionPacket.Action.OPEN_HORSE_INVENTORY;
            case START_FLYING_ELYTRA -> EntityActionPacket.Action.START_FLYING_WITH_ELYTRA;
        };
    }
}
