package com.deyo.hypixel.commons.skyblock;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SkyBlockPlayerProfilesTest {

    @Test
    void serializeHandlesAMissingSelection() {
        UUID player = UUID.randomUUID();
        SkyBlockPlayerProfiles profiles = new SkyBlockPlayerProfiles(player);

        Map<String, Object> serialized = profiles.serialize();

        assertEquals("", serialized.get("selected"));
        assertEquals(List.of(), serialized.get("profiles"));
    }

    @Test
    void deserializeRestoresSelectionAndProfiles() {
        UUID player = UUID.randomUUID();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        SkyBlockPlayerProfiles profiles = SkyBlockPlayerProfiles.deserialize(Map.of(
                "_id", player.toString(),
                "selected", second.toString(),
                "profiles", List.of(first.toString(), second.toString())
        ));

        assertEquals(second, profiles.getCurrentlySelected());
        assertEquals(List.of(first, second), profiles.getProfiles());
    }

    @Test
    void unloadRemovesThePlayerLookup() {
        UUID player = UUID.randomUUID();
        SkyBlockPlayerProfiles profiles = new SkyBlockPlayerProfiles(player);

        assertEquals(profiles, SkyBlockPlayerProfiles.get(player));
        SkyBlockPlayerProfiles.unload(player);
        assertNull(SkyBlockPlayerProfiles.get(player));
    }
}
