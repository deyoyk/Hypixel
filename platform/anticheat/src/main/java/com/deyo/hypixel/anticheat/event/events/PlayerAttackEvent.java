package com.deyo.hypixel.anticheat.event.events;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class PlayerAttackEvent {
    private final DeyoPlayer attacker;
    private final UUID targetUuid;
    private final Pos targetPosition;
    private final long timestamp;

    public PlayerAttackEvent(DeyoPlayer attacker, UUID targetUuid, Pos targetPosition) {
        this.attacker = attacker;
        this.targetUuid = targetUuid;
        this.targetPosition = targetPosition;
        this.timestamp = System.currentTimeMillis();
    }
}
