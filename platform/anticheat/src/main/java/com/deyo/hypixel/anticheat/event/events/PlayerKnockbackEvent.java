package com.deyo.hypixel.anticheat.event.events;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.math.Vel;

@Getter
public class PlayerKnockbackEvent {
    private final DeyoPlayer player;
    private final Vel knockbackVelocity;

    public PlayerKnockbackEvent(DeyoPlayer player, Vel knockbackVelocity) {
        this.player = player;
        this.knockbackVelocity = knockbackVelocity;
    }
}
