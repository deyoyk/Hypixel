package com.deyo.hypixel.service.friend;

import com.deyo.hypixel.commons.presence.PresenceInfo;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void prunesOnlyOldOfflinePresences() {
        UUID stale = UUID.randomUUID();
        UUID recent = UUID.randomUUID();
        UUID online = UUID.randomUUID();
        Map<UUID, PresenceInfo> presences = new HashMap<>();
        presences.put(stale, new PresenceInfo(stale, false, null, null, 1_000L));
        presences.put(recent, new PresenceInfo(recent, false, null, null, 9_500L));
        presences.put(online, new PresenceInfo(online, true, null, null, 1_000L));

        int removed = PresenceStorage.pruneOfflineOlderThan(presences, 10_000L, 5_000L);

        assertEquals(1, removed);
        assertFalse(presences.containsKey(stale));
        assertTrue(presences.containsKey(recent));
        assertTrue(presences.containsKey(online));
    }
}
