package com.deyo.hypixel.type.game.game.event;

import com.deyo.hypixel.type.game.game.AbstractTeamGame;
import com.deyo.hypixel.type.game.game.Game;
import com.deyo.hypixel.type.game.game.team.GameTeam;

public record TeamEliminatedEvent<T extends GameTeam>(
        AbstractTeamGame<?, T> game,
        T team
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
