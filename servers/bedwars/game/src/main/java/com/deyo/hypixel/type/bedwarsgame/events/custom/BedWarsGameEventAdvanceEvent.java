package com.deyo.hypixel.type.bedwarsgame.events.custom;

import com.deyo.hypixel.type.game.game.Game;
import com.deyo.hypixel.type.game.game.event.GameEvent;

public record BedWarsGameEventAdvanceEvent(
        Game<?> game,
    String previousEvent,
    String currentEvent,
    long secondsUntilNext
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
