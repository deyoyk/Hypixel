package com.deyo.hypixel.type.skyblockgeneric.race;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

class RaceManagerTest {

    @Test
    @SuppressWarnings("unchecked")
    void removingPlayerHologramsDropsTheCacheEntry() throws Exception {
        Field field = RaceManager.class.getDeclaredField("perPlayerHolo");
        field.setAccessible(true);
        Map<UUID, RaceManager.HologramCache> holograms =
                (Map<UUID, RaceManager.HologramCache>) field.get(null);

        UUID uuid = UUID.randomUUID();
        holograms.put(uuid, new RaceManager.HologramCache());

        RaceManager.removePlayerHolograms(uuid);

        assertFalse(holograms.containsKey(uuid));
    }
}
