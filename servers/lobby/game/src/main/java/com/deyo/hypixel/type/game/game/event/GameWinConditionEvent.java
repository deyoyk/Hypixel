package com.deyo.hypixel.type.game.game.event;

import com.deyo.hypixel.type.game.game.Game;

public record GameWinConditionEvent(
        Game<?> game
) implements GameEvent {
	@Override
    public Game<?> getGame() {
        return game;
	}
}
