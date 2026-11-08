package com.deyo.hypixel.type.bedwarsgame.events.custom;

import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig.TeamKey;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.Game;
import com.deyo.hypixel.type.game.game.event.GameEvent;

public record BedDestroyedEvent(
        Game<?> game,
    TeamKey teamKey,
    BedWarsPlayer destroyer
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }
}
