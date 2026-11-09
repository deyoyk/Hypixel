package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu;

import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.data.domain.AccountDomain;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUISkyBlockSettingsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void everySettingUsesAUniqueSlotAndToggle() {
        var settings = GUISkyBlockSettings.settings();

        assertEquals(settings.size(), settings.stream().map(GUISkyBlockSettings.Setting::slot).distinct().count());
        assertEquals(settings.size(), settings.stream().map(GUISkyBlockSettings.Setting::toggle).distinct().count());
        assertTrue(settings.stream().anyMatch(setting ->
                setting.toggle() == DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT));
        assertTrue(settings.stream().anyMatch(setting ->
                setting.toggle() == DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES));
    }

    @Test
    void togglingFlipsAndPersistsTheSetting() {
        UUID uuid = UUID.randomUUID();
        PlayerDataService.store(AccountDomain.KEY, uuid, new HypixelDataHandler(uuid));

        try {
            SkyBlockPlayer player = player(uuid);
            GUISkyBlockSettings.Setting setting = GUISkyBlockSettings.settings().getFirst();
            boolean initial = player.getToggles().get(setting.toggle());

            assertEquals(!initial, GUISkyBlockSettings.toggle(player, setting));
            assertEquals(!initial, player.getToggles().get(setting.toggle()));
        } finally {
            PlayerDataService.evict(AccountDomain.KEY, uuid);
        }
    }

    private static SkyBlockPlayer player(UUID uuid) {
        PlayerConnection connection = new PlayerConnection() {
            @Override
            public void sendPacket(SendablePacket packet) {
            }

            @Override
            public SocketAddress getRemoteAddress() {
                return null;
            }
        };
        return new SkyBlockPlayer(new GameProfile(uuid, "Test"), connection);
    }
}
