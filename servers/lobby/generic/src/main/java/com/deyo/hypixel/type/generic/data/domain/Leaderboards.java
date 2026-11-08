package com.deyo.hypixel.type.generic.data.domain;

import com.deyo.hypixel.type.generic.data.DataHandler;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.generic.leaderboard.LeaderboardService;
import com.deyo.hypixel.type.generic.leaderboard.LeaderboardTracked;
import com.deyo.hypixel.type.generic.leaderboard.MapLeaderboardTracked;

import java.util.Map;
import java.util.UUID;

public final class Leaderboards {

    private Leaderboards() {}

    public static void sync(UUID uuid, DataHandler handler) {
        if (!LeaderboardService.isInitialized()) return;

        for (Datapoint<?> datapoint : handler.getDatapoints().values()) {
            if (datapoint instanceof LeaderboardTracked tracked) {
                String key = tracked.getLeaderboardKey();
                if (key != null) {
                    LeaderboardService.updateScore(key, uuid, tracked.getLeaderboardScore());
                }
            }

            if (datapoint instanceof MapLeaderboardTracked mapTracked) {
                Map<String, Double> scores = mapTracked.getAllLeaderboardScores();
                for (Map.Entry<String, Double> entry : scores.entrySet()) {
                    LeaderboardService.updateScore(mapTracked.getLeaderboardKeyFor(entry.getKey()), uuid, entry.getValue());
                }
            }
        }
    }
}
