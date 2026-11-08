package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.math.Pos;

@Getter
public class PositionAndRotationPacket extends DeyoPacket {
    private final Pos pos;
    private final boolean onGround;

    public PositionAndRotationPacket(DeyoPlayer player, Pos pos, boolean onGround) {
        super(player);
        this.pos = pos;
        this.onGround = onGround;
    }

}
