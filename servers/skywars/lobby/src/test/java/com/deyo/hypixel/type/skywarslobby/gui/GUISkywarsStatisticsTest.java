package com.deyo.hypixel.type.skywarslobby.gui;

import com.deyo.hypixel.type.generic.data.datapoints.DatapointSkywarsKitStats;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUISkywarsStatisticsTest {

    @Test
    void challengeStatsSumAcrossKits() {
        DatapointSkywarsKitStats.KitStatistics first = new DatapointSkywarsKitStats.KitStatistics();
        first.addArcherWin();
        first.addArcherWin();
        first.addNoBlockWin();
        DatapointSkywarsKitStats.KitStatistics second = new DatapointSkywarsKitStats.KitStatistics();
        second.addHalfHealthWin();
        second.addUhcWin();

        GUISkywarsStatistics.ChallengeStats stats = GUISkywarsStatistics.challengeStats(
                Map.of("first", first, "second", second));

        assertEquals(2, stats.archer());
        assertEquals(1, stats.halfHealth());
        assertEquals(1, stats.noBlock());
        assertEquals(1, stats.uhc());
        assertEquals(5, stats.total());
    }

    @Test
    void emptyStatsHaveNoChallengeWins() {
        assertEquals(0, GUISkywarsStatistics.challengeStats(Map.of()).total());
    }
}
