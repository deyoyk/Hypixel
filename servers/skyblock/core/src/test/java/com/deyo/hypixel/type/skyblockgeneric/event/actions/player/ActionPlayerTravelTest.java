package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionPlayerTravelTest {

    @Test
    void dungeonHubRequiresCompletedStartingMissionsOrTheTeleporterMission() {
        assertTrue(ActionPlayerTravel.canEnterDungeonHub(true, false));
        assertTrue(ActionPlayerTravel.canEnterDungeonHub(false, true));
        assertFalse(ActionPlayerTravel.canEnterDungeonHub(false, false));
    }
}
