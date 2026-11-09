package com.deyo.hypixel.type.skyblockgeneric.user.statistics;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class PlayerStatisticsBossBarCacheTest {

    @Test
    void removeCachedBossBarDropsTheEntry() {
        UUID playerId = UUID.randomUUID();
        BossBar bar = BossBar.bossBar(Component.empty(), 1f, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
        PlayerStatistics.barCache.put(playerId, bar);

        assertSame(bar, PlayerStatistics.removeCachedBossBar(playerId));
        assertFalse(PlayerStatistics.barCache.containsKey(playerId));
    }
}
