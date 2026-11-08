package com.deyo.hypixel.type.game.game.event;

import net.minestom.server.event.Event;
import com.deyo.hypixel.type.game.game.Game;

public interface GameEvent extends Event {
    Game<?> getGame();
}
