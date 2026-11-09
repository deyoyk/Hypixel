package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.engine.PlayerTickInformation;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TimerFlag extends Flag {
    // Track last packet times per player
    private static final Map<UUID, Long> lastPacketTimes = new ConcurrentHashMap<>();
    private static final Map<UUID, Integer> packetCounts = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> intervalTotals = new ConcurrentHashMap<>();

    public static void clearPlayer(UUID uuid) {
        lastPacketTimes.remove(uuid);
        packetCounts.remove(uuid);
        intervalTotals.remove(uuid);
    }

    // Normal tick length is 50ms
    private static final long EXPECTED_TICK_MS = 50;
    private static final double MIN_TICK_RATIO = 0.7; // 70% of normal speed (35ms) is suspicious
    private static final int SAMPLE_SIZE = 20; // Check over 20 ticks

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        DeyoPlayer player = event.getPlayer();
        UUID uuid = player.getUuid();
        long currentTime = System.currentTimeMillis();

        Long lastTime = lastPacketTimes.put(uuid, currentTime);
        if (lastTime == null) {
            packetCounts.put(uuid, 0);
            intervalTotals.put(uuid, 0L);
            return;
        }

        long timeDiff = currentTime - lastTime;
        long totalTime = intervalTotals.merge(uuid, timeDiff, Long::sum);
        int count = packetCounts.merge(uuid, 1, Integer::sum);

        // Only check after we have enough samples
        if (count < SAMPLE_SIZE) {
            return;
        }

        // Calculate average tick time over recent samples
        java.util.List<PlayerTickInformation> ticks = player.getLastTicks();
        if (ticks.size() < 5) return;

        double tickRatio = averageInterval(totalTime, count) / EXPECTED_TICK_MS;

        if (tickRatio < MIN_TICK_RATIO) {
            // Player is sending packets faster than normal game speed
            // This indicates timer/game speed manipulation
            double speedMultiplier = MIN_TICK_RATIO / tickRatio;

            // Higher speed = higher certainty
            // 1.5x speed = 70%, 2x speed = 85%, 3x+ = 95%
            double certainty = Math.min(0.95, 0.5 + (speedMultiplier - 1.0) * 0.3);

            player.flag(com.deyo.hypixel.anticheat.flag.FlagType.TIMER, certainty);
        }

        // Start a fresh sample window after each measurement.
        packetCounts.put(uuid, 0);
        intervalTotals.put(uuid, 0L);
    }

    static double averageInterval(long totalMs, int samples) {
        return samples <= 0 ? 0 : (double) totalMs / samples;
    }

    static boolean isTooFast(double averageIntervalMs) {
        return averageIntervalMs < EXPECTED_TICK_MS * MIN_TICK_RATIO;
    }
}
