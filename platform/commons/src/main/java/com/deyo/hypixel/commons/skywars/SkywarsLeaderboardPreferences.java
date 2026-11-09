package com.deyo.hypixel.commons.skywars;

import com.deyo.hypixel.commons.leaderboard.LeaderboardPlayerFilter;

public record SkywarsLeaderboardPreferences(
        SkywarsLeaderboardPeriod period,
        SkywarsLeaderboardMode mode,
        SkywarsLeaderboardView view,
        SkywarsTextAlignment textAlignment,
        LeaderboardPlayerFilter playerFilter
) {
    public SkywarsLeaderboardPreferences {
        if (playerFilter == null) playerFilter = LeaderboardPlayerFilter.ALL;
    }

    public static SkywarsLeaderboardPreferences defaults() {
        return new SkywarsLeaderboardPreferences(
                SkywarsLeaderboardPeriod.WEEKLY,
                SkywarsLeaderboardMode.ALL,
                SkywarsLeaderboardView.TOP_10,
                SkywarsTextAlignment.CENTER,
                LeaderboardPlayerFilter.ALL
        );
    }
}
