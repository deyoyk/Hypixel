package com.deyo.hypixel.anticheat.event.events;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.PlayerTickInformation;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

@Getter
public class PlayerPositionUpdateEvent {
    private final DeyoPlayer player;
    private final PlayerTickInformation previousTick;
    private final PlayerTickInformation currentTick;

    public PlayerPositionUpdateEvent(DeyoPlayer player, PlayerTickInformation previousTick,
                                     PlayerTickInformation currentTick) {
        this.player = player;
        this.previousTick = previousTick;
        this.currentTick = currentTick;
    }
}
