package com.deyo.hypixel.type.skyblockgeneric.user;

import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointString;
import com.deyo.hypixel.type.generic.data.domain.AccountDomain;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkyBlockPlayerNameTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void rawNameUsesTheOnlineOrCachedAccountName() {
        UUID uuid = UUID.randomUUID();
        HypixelDataHandler handler = new HypixelDataHandler(uuid);
        handler.get(HypixelDataHandler.Data.IGN, DatapointString.class).setValue("OfflinePlayer");
        PlayerDataService.store(AccountDomain.KEY, uuid, handler);

        try {
            assertEquals("OfflinePlayer", SkyBlockPlayer.getRawName(uuid));
        } finally {
            PlayerDataService.evict(AccountDomain.KEY, uuid);
        }
    }
}
