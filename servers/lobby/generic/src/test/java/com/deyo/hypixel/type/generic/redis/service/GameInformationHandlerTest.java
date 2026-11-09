package com.deyo.hypixel.type.generic.redis.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameInformationHandlerTest {

    @Test
    void clearRemovesTheAssignedGame() {
        UUID playerId = UUID.randomUUID();
        GameInformationHandler.game.put(playerId, "game-1");

        GameInformationHandler.clear(playerId);

        assertFalse(GameInformationHandler.game.containsKey(playerId));
    }

    @Test
    void clearKeepsOtherAssignments() {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        GameInformationHandler.game.put(first, "game-1");
        GameInformationHandler.game.put(second, "game-2");

        GameInformationHandler.clear(first);

        assertTrue(GameInformationHandler.game.containsKey(second));
        GameInformationHandler.clear(second);
    }
}
