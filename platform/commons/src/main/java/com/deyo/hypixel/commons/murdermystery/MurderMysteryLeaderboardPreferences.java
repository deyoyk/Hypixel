package com.deyo.hypixel.commons.murdermystery;

import com.deyo.hypixel.commons.leaderboard.LeaderboardPlayerFilter;

public record MurderMysteryLeaderboardPreferences(
        MurderMysteryLeaderboardPeriod period,
        MurderMysteryLeaderboardMode mode,
        MurderMysteryLeaderboardView view,
        MurderMysteryTextAlignment textAlignment,
        LeaderboardPlayerFilter playerFilter
) {
    public MurderMysteryLeaderboardPreferences {
        if (playerFilter == null) playerFilter = LeaderboardPlayerFilter.ALL;
    }

    public static MurderMysteryLeaderboardPreferences defaults() {
        return new MurderMysteryLeaderboardPreferences(
                MurderMysteryLeaderboardPeriod.WEEKLY,
                MurderMysteryLeaderboardMode.CLASSIC,
                MurderMysteryLeaderboardView.TOP_10,
                MurderMysteryTextAlignment.CENTER,
                LeaderboardPlayerFilter.ALL
        );
    }
}
