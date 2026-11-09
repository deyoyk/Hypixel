package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.friend.Friend;
import com.deyo.hypixel.commons.friend.FriendData;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.presence.PresenceInfo;
import com.deyo.hypixel.type.generic.friend.FriendManager;
import com.deyo.hypixel.type.generic.guild.GuildManager;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SocialCountCache {
    private static final long CACHE_TTL_MS = 30_000;

    private static final Map<UUID, Counts> CACHE = new ConcurrentHashMap<>();
    private static final Set<UUID> REFRESHING = ConcurrentHashMap.newKeySet();

    private SocialCountCache() {
    }

    public static int getFriendsOnline(HypixelPlayer player) {
        Counts counts = get(player);
        refreshIfStale(player, counts);
        return counts.friendsOnline();
    }

    public static int getGuildOnline(HypixelPlayer player) {
        Counts counts = get(player);
        refreshIfStale(player, counts);
        return counts.guildOnline();
    }

    public static void clear(UUID playerUuid) {
        CACHE.remove(playerUuid);
        REFRESHING.remove(playerUuid);
    }

    static int countOnline(List<PresenceInfo> presence) {
        int online = 0;
        for (PresenceInfo info : presence) {
            if (info != null && info.isOnline()) online++;
        }
        return online;
    }

    static List<UUID> friendIds(FriendData data) {
        return data.getFriends().stream().map(Friend::getUuid).toList();
    }

    private static Counts get(HypixelPlayer player) {
        return CACHE.computeIfAbsent(player.getUuid(), ignored -> new Counts(0, 0, 0));
    }

    private static void refreshIfStale(HypixelPlayer player, Counts counts) {
        if (System.currentTimeMillis() - counts.updatedAt() <= CACHE_TTL_MS) return;
        if (!REFRESHING.add(player.getUuid())) return;

        Thread.startVirtualThread(() -> {
            int friendsOnline = counts.friendsOnline();
            int guildOnline = counts.guildOnline();
            try {
                FriendData friendData = FriendManager.getFriendData(player);
                if (friendData != null) {
                    friendsOnline = countOnline(FriendManager.getPresenceBulk(friendIds(friendData)));
                }

                GuildData guildData = GuildManager.getGuildFromPlayer(player);
                if (guildData != null) {
                    guildOnline = countOnline(FriendManager.getPresenceBulk(guildData.getAllMemberUuids()));
                }
            } catch (RuntimeException ignored) {
                // Keep the previous values; the next refresh will retry.
            } finally {
                CACHE.put(player.getUuid(), new Counts(friendsOnline, guildOnline, System.currentTimeMillis()));
                REFRESHING.remove(player.getUuid());
            }
        });
    }

    private record Counts(int friendsOnline, int guildOnline, long updatedAt) {
    }
}
