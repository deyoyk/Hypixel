package com.deyo.hypixel.anticheat.loader.minestom.packets;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.network.packet.client.play.ClientPlayerBlockPlacementPacket;
import com.deyo.hypixel.anticheat.event.packet.BlockPlacePacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.LoaderPacketHandler;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

public class MinestomHandlerBlockPlacePacket
        extends LoaderPacketHandler<ClientPlayerBlockPlacementPacket> {

    @Override
    public DeyoPacket buildDeyoPacket(UUID uuid, ClientPlayerBlockPlacementPacket packet) {
        BlockPlacePacket.Hand hand = packet.hand() == PlayerHand.MAIN ?
                BlockPlacePacket.Hand.MAIN_HAND : BlockPlacePacket.Hand.OFF_HAND;
        BlockPlacePacket.Direction direction = mapDirection(packet.blockFace());
        Pos position = new Pos(
                packet.blockPosition().blockX(),
                packet.blockPosition().blockY(),
                packet.blockPosition().blockZ()
        );

        return new BlockPlacePacket(
                uuid, hand, position, direction,
                packet.cursorPositionX(), packet.cursorPositionY(), packet.cursorPositionZ(),
                packet.insideBlock(), packet.sequence()
        );
    }

    @Override
    public ClientPlayerBlockPlacementPacket buildLoaderPacket(UUID uuid, DeyoPacket packet) {
        return null;
    }

    @Override
    public Class<ClientPlayerBlockPlacementPacket> getHandledPacketClass() {
        return ClientPlayerBlockPlacementPacket.class;
    }

    private BlockPlacePacket.Direction mapDirection(net.minestom.server.instance.block.BlockFace face) {
        return switch (face) {
            case BOTTOM -> BlockPlacePacket.Direction.DOWN;
            case TOP -> BlockPlacePacket.Direction.UP;
            case NORTH -> BlockPlacePacket.Direction.NORTH;
            case SOUTH -> BlockPlacePacket.Direction.SOUTH;
            case WEST -> BlockPlacePacket.Direction.WEST;
            case EAST -> BlockPlacePacket.Direction.EAST;
        };
    }
}
