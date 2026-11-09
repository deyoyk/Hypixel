package com.deyo.hypixel.commons.bedwars;

import com.deyo.hypixel.commons.leaderboard.LeaderboardPlayerFilter;

public record LeaderboardPreferences(
		BedwarsLeaderboardPeriod period,
		BedwarsLeaderboardMode mode,
		BedwarsLeaderboardView view,
		BedwarsTextAlignment textAlignment,
		LeaderboardPlayerFilter playerFilter
) {
	public LeaderboardPreferences {
		if (playerFilter == null) playerFilter = LeaderboardPlayerFilter.ALL;
	}

	public static LeaderboardPreferences defaults() {
		return new LeaderboardPreferences(
				BedwarsLeaderboardPeriod.LIFETIME,
				BedwarsLeaderboardMode.ALL,
				BedwarsLeaderboardView.TOP_10,
				BedwarsTextAlignment.CENTER,
				LeaderboardPlayerFilter.ALL
		);
	}
}
