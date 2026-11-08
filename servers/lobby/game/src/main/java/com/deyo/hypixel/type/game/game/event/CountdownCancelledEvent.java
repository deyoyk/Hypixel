package com.deyo.hypixel.type.game.game.event;

import com.deyo.hypixel.type.game.game.Game;

public record CountdownCancelledEvent(
        Game<?> game,
	String reason
) implements GameEvent {
	@Override
    public Game<?> getGame() {
        return game;
	}
}
