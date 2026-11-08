package com.deyo.hypixel.type.game.game.event;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.type.game.game.Game;

public record PlayerLeaveGameEvent(
        Game<?> game,
        Player player,
        LeaveReason reason
) implements GameEvent {
    @Override
    public Game<?> getGame() {
        return game;
    }

    public enum LeaveReason {
        VOLUNTARY,
        KICKED,
        ELIMINATED,
        GAME_END
    }
}
