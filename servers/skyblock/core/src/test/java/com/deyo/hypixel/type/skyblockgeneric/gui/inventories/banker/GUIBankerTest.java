package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.banker;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDomain;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointBankData;
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

class GUIBankerTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void vaultRequiresCoinsAndOnlyChargesOnce() {
        UUID uuid = UUID.randomUUID();
        PlayerDataService.store(SkyBlockDomain.KEY, uuid,
                SkyBlockDataHandler.initUserWithDefaultData(uuid, UUID.randomUUID()));

        try {
            SkyBlockPlayer player = player(uuid);
            DatapointBankData.BankData bankData = bankData(player);

            player.setCoins(9_999);
            assertFalse(GUIBanker.unlockPersonalVault(player, bankData));
            assertEquals(9_999, player.getCoins());
            assertFalse(bankData.isPersonalVaultUnlocked());

            player.setCoins(10_000);
            assertTrue(GUIBanker.unlockPersonalVault(player, bankData));
            assertEquals(0, player.getCoins());
            assertTrue(bankData.isPersonalVaultUnlocked());
            assertTrue(GUIBanker.unlockPersonalVault(player, bankData));
            assertEquals(0, player.getCoins());
        } finally {
            PlayerDataService.evict(SkyBlockDomain.KEY, uuid);
        }
    }

    private static DatapointBankData.BankData bankData(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler()
                .get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class)
                .getValue();
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
