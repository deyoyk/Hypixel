package com.deyo.hypixel.type.lobby.parkour;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

class LobbyParkourManagerTest {

    @Test
    @SuppressWarnings("unchecked")
    void removingPlayerHologramsDropsTheCacheEntry() throws Exception {
        Field field = LobbyParkourManager.class.getDeclaredField("perPlayerHolo");
        field.setAccessible(true);
        Map<UUID, LobbyParkourManager.HologramCache> holograms =
                (Map<UUID, LobbyParkourManager.HologramCache>) field.get(null);

        UUID uuid = UUID.randomUUID();
        holograms.put(uuid, new LobbyParkourManager.HologramCache());

        LobbyParkourManager.removePlayerHolograms(uuid);

        assertFalse(holograms.containsKey(uuid));
    }
}
