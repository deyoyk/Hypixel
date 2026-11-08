package com.deyo.hypixel.type.game.game.event;

import com.deyo.hypixel.type.game.game.Game;

public record CountdownTickEvent(
        Game<?> game,
        int remainingSeconds,
        boolean shouldAnnounce
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }

    public boolean isComplete() {
        return remainingSeconds <= 0;
    }
}
