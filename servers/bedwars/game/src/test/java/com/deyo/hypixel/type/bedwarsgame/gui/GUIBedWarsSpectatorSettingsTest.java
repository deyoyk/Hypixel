package com.deyo.hypixel.type.bedwarsgame.gui;

import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.SendablePacket;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.potion.PotionEffect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIBedWarsSpectatorSettingsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void flySpeedCyclesAndClamps() {
        assertEquals(2, GUIBedWarsSpectatorSettings.nextFlySpeed(1));
        assertEquals(5, GUIBedWarsSpectatorSettings.nextFlySpeed(4));
        assertEquals(1, GUIBedWarsSpectatorSettings.nextFlySpeed(5));
        assertEquals(1, GUIBedWarsSpectatorSettings.nextFlySpeed(0));
        assertEquals(1, GUIBedWarsSpectatorSettings.nextFlySpeed(9));
    }

    @Test
    void flySpeedConvertsForMinestom() {
        assertEquals(0.3f, GUIBedWarsSpectatorSettings.minestomFlySpeed(3), 0.0001f);
        assertEquals(0.1f, GUIBedWarsSpectatorSettings.minestomFlySpeed(0), 0.0001f);
        assertEquals(1.0f, GUIBedWarsSpectatorSettings.minestomFlySpeed(10), 0.0001f);
        assertEquals(1.0f, GUIBedWarsSpectatorSettings.minestomFlySpeed(20), 0.0001f);
    }

    @Test
    void settingsAreAppliedToThePlayer() {
        BedWarsPlayer player = player("test");

        player.setSpectatorNightVision(true);
        GUIBedWarsSpectatorSettings.applyNightVision(player);
        assertTrue(player.hasEffect(PotionEffect.NIGHT_VISION));

        player.setSpectatorNightVision(false);
        GUIBedWarsSpectatorSettings.applyNightVision(player);
        assertFalse(player.hasEffect(PotionEffect.NIGHT_VISION));

        player.setSpectatorFlySpeed(3);
        GUIBedWarsSpectatorSettings.applyFlySpeed(player);
        assertEquals(0.3f, player.getFlyingSpeed(), 0.0001f);
    }

    private static BedWarsPlayer player(String name) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new BedWarsPlayer(connection, new GameProfile(UUID.randomUUID(), name));
    }
}
