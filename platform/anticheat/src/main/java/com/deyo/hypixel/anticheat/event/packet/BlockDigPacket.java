package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class BlockDigPacket extends DeyoPacket {
    private final Status status;
    private final Pos position;
    private final Direction direction;
    private final int sequence;

    public BlockDigPacket(UUID uuid, Status status, Pos position, Direction direction, int sequence) {
        super(uuid);
        this.status = status;
        this.position = position;
        this.direction = direction;
        this.sequence = sequence;
    }

    public enum Status {
        STARTED_DIGGING,
        CANCELLED_DIGGING,
        FINISHED_DIGGING,
        DROP_ITEM_STACK,
        DROP_ITEM,
        UPDATE_HELD_ITEM,
        SWAP_ITEM_IN_HAND,
        STAB
    }

    public enum Direction {
        DOWN, UP, NORTH, SOUTH, WEST, EAST
    }
}
