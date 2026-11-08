package com.deyo.hypixel.type.game.game.event;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.type.game.game.AbstractGame;
import com.deyo.hypixel.type.game.game.Game;

public record PlayerRejoinGameEvent(
        Game<?> gameId,
        Player player,
        AbstractGame.DisconnectedPlayerData disconnectedPlayerData
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return gameId;
    }
}
