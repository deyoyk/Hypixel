package com.deyo.hypixel.commons.bedwars.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BedWarsMapsConfigTeamKeyTest {

    @Test
    void resolvesTeamNamesCaseInsensitively() {
        assertEquals(BedWarsMapsConfig.TeamKey.RED,
                BedWarsMapsConfig.TeamKey.fromName("RED"));
        assertEquals(BedWarsMapsConfig.TeamKey.AQUA,
                BedWarsMapsConfig.TeamKey.fromName("aqua"));
        assertEquals(BedWarsMapsConfig.TeamKey.PINK,
                BedWarsMapsConfig.TeamKey.fromName("PINK"));
    }

    @Test
    void unknownAndMissingNamesReturnNull() {
        assertNull(BedWarsMapsConfig.TeamKey.fromName(null));
        assertNull(BedWarsMapsConfig.TeamKey.fromName(""));
        assertNull(BedWarsMapsConfig.TeamKey.fromName("   "));
        assertNull(BedWarsMapsConfig.TeamKey.fromName("PURPLE"));
    }
}
