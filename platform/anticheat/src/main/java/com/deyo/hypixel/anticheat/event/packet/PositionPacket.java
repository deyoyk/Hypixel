package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import org.jetbrains.annotations.NotNull;

@Getter
public class PositionPacket extends DeyoPacket {
    private final double x;
    private final double y;
    private final double z;
    private final boolean onGround;

    public PositionPacket(@NotNull DeyoPlayer player, double x, double y, double z, boolean onGround) {
        super(player);
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }
}
