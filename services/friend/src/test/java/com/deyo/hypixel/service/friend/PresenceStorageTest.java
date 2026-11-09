package com.deyo.hypixel.service.friend;

import com.deyo.hypixel.commons.presence.PresenceInfo;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PresenceStorageTest {

    @Test
    void preservesServerMetadataAndNewestLastSeen() {
        UUID uuid = UUID.randomUUID();
        PresenceStorage.upsertPreservingServer(new PresenceInfo(uuid, true, "SKYBLOCK_HUB", "H1", 1_000L));

        PresenceStorage.upsertPreservingServer(new PresenceInfo(uuid, false, null, null, 500L));

        PresenceInfo merged = PresenceStorage.get(uuid);
        assertEquals("SKYBLOCK_HUB", merged.getServerType());
        assertEquals("H1", merged.getServerId());
        assertEquals(1_000L, merged.getLastSeen());
        assertFalse(merged.isOnline());
    }

    @Test
    void incomingLastSeenAdvancesTheStoredValue() {
        UUID uuid = UUID.randomUUID();
        PresenceStorage.upsertPreservingServer(new PresenceInfo(uuid, true, "SKYBLOCK_HUB", "H1", 1_000L));

        PresenceStorage.upsertPreservingServer(new PresenceInfo(uuid, true, "SKYBLOCK_HUB", "H1", 2_000L));

        assertEquals(2_000L, PresenceStorage.get(uuid).getLastSeen());
    }
}
