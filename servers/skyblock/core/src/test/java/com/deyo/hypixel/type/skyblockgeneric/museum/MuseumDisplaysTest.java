package com.deyo.hypixel.type.skyblockgeneric.museum;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MuseumDisplaysTest {

    @Test
    void clearDisplayRemovesCachedEntities() {
        UUID playerId = UUID.randomUUID();
        MuseumDisplays.displayEntities.put(playerId, new HashMap<>());

        MuseumDisplays.clearDisplay(playerId);

        assertFalse(MuseumDisplays.displayEntities.containsKey(playerId));
    }

    @Test
    void clearDisplayToleratesMissingEntries() {
        assertDoesNotThrow(() -> MuseumDisplays.clearDisplay(UUID.randomUUID()));
    }
}
