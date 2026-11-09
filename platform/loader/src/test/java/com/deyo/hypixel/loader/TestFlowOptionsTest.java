package com.deyo.hypixel.loader;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestFlowOptionsTest {

    @Test
    void missingAndBlankConfigsAreIgnored() {
        assertTrue(TestFlowOptions.parseServerConfigs(null).isEmpty());
        assertTrue(TestFlowOptions.parseServerConfigs("").isEmpty());
        assertTrue(TestFlowOptions.parseServerConfigs("   ").isEmpty());
    }

    @Test
    void parsesTypesWithOptionalCounts() {
        List<Map<String, Object>> configs = TestFlowOptions.parseServerConfigs(
                "SKYBLOCK_HUB:2, BEDWARS_GAME:3, PROTOTYPE_LOBBY");

        assertEquals(3, configs.size());
        assertEquals("SKYBLOCK_HUB", configs.get(0).get("type"));
        assertEquals(2, configs.get(0).get("count"));
        assertEquals("BEDWARS_GAME", configs.get(1).get("type"));
        assertEquals(3, configs.get(1).get("count"));
        assertEquals("PROTOTYPE_LOBBY", configs.get(2).get("type"));
        assertEquals(1, configs.get(2).get("count"));
    }

    @Test
    void skipsEmptyEntries() {
        List<Map<String, Object>> configs = TestFlowOptions.parseServerConfigs(
                "SKYBLOCK_HUB:2, :3, , BEDWARS_LOBBY");

        assertEquals(2, configs.size());
        assertEquals("SKYBLOCK_HUB", configs.get(0).get("type"));
        assertEquals("BEDWARS_LOBBY", configs.get(1).get("type"));
    }
}
