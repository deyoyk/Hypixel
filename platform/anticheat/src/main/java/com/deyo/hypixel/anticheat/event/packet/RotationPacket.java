package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

@Getter
public class RotationPacket extends DeyoPacket {
    private final float yaw;
    private final float pitch;
    private final boolean onGround;

    public RotationPacket(DeyoPlayer player, float yaw, float pitch, boolean onGround) {
        super(player);
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

}
