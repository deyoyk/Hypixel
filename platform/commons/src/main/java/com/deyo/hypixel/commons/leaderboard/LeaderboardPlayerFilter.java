package com.deyo.hypixel.commons.leaderboard;

import lombok.Getter;

@Getter
public enum LeaderboardPlayerFilter {
    ALL("All"),
    FRIENDS("Friends"),
    BEST_FRIENDS("Best Friends"),
    GUILD_MEMBERS("Guild Members");

    private final String displayName;

    LeaderboardPlayerFilter(String displayName) {
        this.displayName = displayName;
    }

    public LeaderboardPlayerFilter next() {
        LeaderboardPlayerFilter[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    public LeaderboardPlayerFilter previous() {
        LeaderboardPlayerFilter[] all = values();
        return all[(ordinal() - 1 + all.length) % all.length];
    }
}
