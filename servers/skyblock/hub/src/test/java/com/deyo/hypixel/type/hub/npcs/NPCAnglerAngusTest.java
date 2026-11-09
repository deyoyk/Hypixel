package com.deyo.hypixel.type.hub.npcs;

import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NPCAnglerAngusTest {

    @Test
    void firstBaitRewardIsOnlyClaimedOnce() {
        DatapointToggles.Toggles toggles = new DatapointToggles.Toggles(new HashMap<>());

        assertTrue(NPCAnglerAngus.claimFirstBait(toggles));
        assertFalse(NPCAnglerAngus.claimFirstBait(toggles));
        assertTrue(toggles.get(DatapointToggles.Toggles.ToggleType.HAS_CLAIMED_ANGLER_ANGUS_BAIT));
    }

    @Test
    void baitGuideIsUsable() {
        assertNotNull(NPCAnglerAngus.baitGuide());
    }
}
