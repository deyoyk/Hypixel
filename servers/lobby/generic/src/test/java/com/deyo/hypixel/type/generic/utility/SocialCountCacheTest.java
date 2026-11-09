package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.friend.Friend;
import com.deyo.hypixel.commons.friend.FriendData;
import com.deyo.hypixel.commons.friend.FriendSettings;
import com.deyo.hypixel.commons.presence.PresenceInfo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SocialCountCacheTest {

    @Test
    void countsOnlyOnlinePresenceEntries() {
        List<PresenceInfo> presence = List.of(
                presence(true),
                presence(false),
                presence(true)
        );

        assertEquals(2, SocialCountCache.countOnline(presence));
        assertEquals(0, SocialCountCache.countOnline(List.of()));
    }

    @Test
    void extractsFriendIdsWithoutDuplicatesBeingIntroduced() {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        FriendData data = new FriendData(UUID.randomUUID(), List.of(
                Friend.create(first),
                Friend.create(second)
        ), FriendSettings.createDefault());

        assertEquals(List.of(first, second), SocialCountCache.friendIds(data));
    }

    private static PresenceInfo presence(boolean online) {
        return new PresenceInfo(UUID.randomUUID(), online, null, null, 0);
    }
}
