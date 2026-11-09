package com.deyo.hypixel.type.skyblockgeneric.fishing.ship;

import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointShipState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FishingShipServiceTest {

    @Test
    void normalizesValidShipNames() {
        assertEquals("Sea Breeze", FishingShipService.normalizeShipName("  §bSea Breeze  "));
        assertEquals("x".repeat(32), FishingShipService.normalizeShipName("x".repeat(32)));
    }

    @Test
    void rejectsBlankAndOversizedShipNames() {
        assertNull(FishingShipService.normalizeShipName(null));
        assertNull(FishingShipService.normalizeShipName("   "));
        assertNull(FishingShipService.normalizeShipName("§c§l"));
        assertNull(FishingShipService.normalizeShipName("x".repeat(33)));
    }

    @Test
    void appliesOnlyValidNamesToShipState() {
        DatapointShipState.ShipState state = new DatapointShipState.ShipState();

        assertTrue(FishingShipService.applyShipName(state, "§6Golden Tide"));
        assertEquals("Golden Tide", state.getShipName());

        assertFalse(FishingShipService.applyShipName(state, " "));
        assertEquals("Golden Tide", state.getShipName());
    }
}
