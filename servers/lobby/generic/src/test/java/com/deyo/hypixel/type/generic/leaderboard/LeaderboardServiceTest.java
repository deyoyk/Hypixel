package com.deyo.hypixel.type.generic.leaderboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LeaderboardServiceTest {

    @Test
    void clearLeaderboardIsSafeWhenRedisIsUnavailable() {
        assertFalse(LeaderboardService.clearLeaderboard("bedwars:test"));
    }
}
