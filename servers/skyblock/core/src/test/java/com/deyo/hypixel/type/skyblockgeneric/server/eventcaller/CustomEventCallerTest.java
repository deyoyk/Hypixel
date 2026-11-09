package com.deyo.hypixel.type.skyblockgeneric.server.eventcaller;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomEventCallerTest {

    @Test
    void initializesEveryMissingPlayerInOnePass() {
        Map<String, Boolean> cache = new HashMap<>();
        List<String> initialized = new ArrayList<>();

        CustomEventCaller.initializeMissing(
                List.of("first", "second", "third"),
                cache,
                player -> {
                    initialized.add(player);
                    cache.put(player, true);
                });

        assertEquals(List.of("first", "second", "third"), initialized);
    }

    @Test
    void skipsPlayersThatAreAlreadyCached() {
        Map<String, Boolean> cache = new HashMap<>();
        cache.put("cached", true);
        List<String> initialized = new ArrayList<>();

        CustomEventCaller.initializeMissing(
                List.of("cached", "new"),
                cache,
                player -> {
                    initialized.add(player);
                    cache.put(player, true);
                });

        assertEquals(List.of("new"), initialized);
    }
}
