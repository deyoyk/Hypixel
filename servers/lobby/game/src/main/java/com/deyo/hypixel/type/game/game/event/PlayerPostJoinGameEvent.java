package com.deyo.hypixel.type.game.game.event;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.type.game.game.Game;

public record PlayerPostJoinGameEvent(
        Game<?> game,
        Player player,
        int currentPlayerCount,
        int maxPlayerCount
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
