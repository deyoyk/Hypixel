package com.deyo.hypixel.type.game.game.event;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.type.game.game.Game;

public record PlayerAssignedTeamEvent<T>(
        Game<?> game,
        Player player,
        T team
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
