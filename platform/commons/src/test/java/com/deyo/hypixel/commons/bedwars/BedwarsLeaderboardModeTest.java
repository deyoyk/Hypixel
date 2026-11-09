package com.deyo.hypixel.commons.bedwars;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BedwarsLeaderboardModeTest {

    @Test
    void dreamGameTypesUseASeparateMode() {
        assertEquals(BedwarsLeaderboardMode.DREAM,
                BedwarsLeaderboardMode.fromGameType(BedWarsGameType.ULTIMATE_DOUBLES));
        assertEquals(BedwarsLeaderboardMode.DREAM,
                BedwarsLeaderboardMode.fromGameType(BedWarsGameType.RUSH_DOUBLES));
        assertEquals(BedwarsLeaderboardMode.SOLO,
                BedwarsLeaderboardMode.fromGameType(BedWarsGameType.ONE_EIGHT));

        assertTrue(BedwarsLeaderboardMode.DREAM.includes(BedWarsGameType.SWAPPAGE_FOURS));
        assertFalse(BedwarsLeaderboardMode.DREAM.includes(BedWarsGameType.ONE_EIGHT));
        assertFalse(BedwarsLeaderboardMode.CORE.includes(BedWarsGameType.RUSH_DOUBLES));
    }

    @Test
    void dreamStatsAreRecordedSeparatelyFromCoreModes() {
        BedWarsModeStats stats = BedWarsModeStats.empty();
        stats.recordWin(BedwarsLeaderboardMode.DREAM);
        stats.recordWin(BedwarsLeaderboardMode.ALL);

        assertEquals(1, stats.getWins(BedwarsLeaderboardMode.DREAM, BedwarsLeaderboardPeriod.LIFETIME));
        assertEquals(1, stats.getWins(BedwarsLeaderboardMode.ALL, BedwarsLeaderboardPeriod.LIFETIME));
        assertEquals(0, stats.getWins(BedwarsLeaderboardMode.CORE, BedwarsLeaderboardPeriod.LIFETIME));
    }
}
