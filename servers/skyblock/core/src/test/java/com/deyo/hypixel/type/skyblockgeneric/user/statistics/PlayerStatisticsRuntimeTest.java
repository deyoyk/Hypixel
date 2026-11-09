package com.deyo.hypixel.type.skyblockgeneric.user.statistics;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerStatisticsRuntimeTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void runtimeValuesIncludeAbsorptionAndRoundForDisplay() {
        PlayerStatistics.RuntimeValues values = PlayerStatistics.runtimeValues(
                10.4f, 2f, 50.6f, 20.6f, 12.4, 120.5f);

        assertEquals(12, values.hearts());
        assertEquals(21, values.maxHearts());
        assertEquals(12.4, values.defense());
        assertEquals(51, values.mana());
        assertEquals(121, values.maxMana());
        assertEquals(2f, values.absorption());
    }

    @Test
    void healthRegenerationUsesTheCurrentMaxHealthAndBonus() {
        assertEquals(2.5f, PlayerStatistics.healthRegenerationAmount(100f, 0));
        assertEquals(3.75f, PlayerStatistics.healthRegenerationAmount(100f, 0.5));
        assertEquals(2.0f, PlayerStatistics.healthRegenerationAmount(50.9f, 0));
    }

    @Test
    void temporaryStatPresenceTracksActiveBuffs() {
        PlayerStatistics statistics = new PlayerStatistics(player());
        assertFalse(statistics.hasTemporaryStatistics());

        statistics.boostStatistic(new TemporaryStatistic(
                ItemStatistics.empty(), System.currentTimeMillis() + 10_000));

        assertTrue(statistics.hasTemporaryStatistics());
    }

    @Test
    void movementSpeedUsesTheSpeedStatistic() {
        ItemStatistics statistics = ItemStatistics.builder()
                .withBase(ItemStatistic.SPEED, 250D)
                .build();

        assertEquals(0.25f, PlayerStatistics.movementSpeed(statistics));
    }

    private static SkyBlockPlayer player() {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new SkyBlockPlayer(new GameProfile(UUID.randomUUID(), "test"), connection);
    }
}
