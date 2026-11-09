package com.deyo.hypixel.type.generic.leaderboard;

import com.deyo.hypixel.commons.friend.Friend;
import com.deyo.hypixel.commons.friend.FriendData;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.leaderboard.LeaderboardPlayerFilter;
import com.deyo.hypixel.type.generic.friend.FriendManager;
import com.deyo.hypixel.type.generic.guild.GuildManager;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class LeaderboardPlayerFilterResolver {
    private LeaderboardPlayerFilterResolver() {
    }

    public static AllowedPlayers resolve(HypixelPlayer viewer, LeaderboardPlayerFilter filter) {
        return switch (filter) {
            case ALL -> AllowedPlayers.all();
            case FRIENDS -> friends(viewer, false);
            case BEST_FRIENDS -> friends(viewer, true);
            case GUILD_MEMBERS -> guildMembers(viewer);
        };
    }

    public static List<LeaderboardService.LeaderboardEntry> limitEntries(
            List<LeaderboardService.LeaderboardEntry> entries, AllowedPlayers allowed, int limit) {
        List<LeaderboardService.LeaderboardEntry> result = new ArrayList<>(Math.min(limit, entries.size()));
        for (LeaderboardService.LeaderboardEntry entry : entries) {
            if (!allowed.allows(entry.playerUuid())) continue;
            result.add(entry);
            if (result.size() == limit) break;
        }
        return result;
    }

    private static AllowedPlayers friends(HypixelPlayer viewer, boolean bestOnly) {
        FriendData data = FriendManager.getFriendData(viewer);
        if (data == null) return AllowedPlayers.none();

        Set<UUID> uuids = new HashSet<>();
        for (Friend friend : data.getFriends()) {
            if (!bestOnly || friend.isBestFriend()) uuids.add(friend.getUuid());
        }
        return AllowedPlayers.of(uuids);
    }

    private static AllowedPlayers guildMembers(HypixelPlayer viewer) {
        GuildData guild = GuildManager.getGuildFromPlayer(viewer);
        if (guild == null) return AllowedPlayers.none();
        return AllowedPlayers.of(new HashSet<>(guild.getAllMemberUuids()));
    }

    public record AllowedPlayers(boolean unrestricted, Set<UUID> playerUuids) {
        public static AllowedPlayers all() {
            return new AllowedPlayers(true, Set.of());
        }

        public static AllowedPlayers none() {
            return new AllowedPlayers(false, Set.of());
        }

        public static AllowedPlayers of(Set<UUID> playerUuids) {
            return new AllowedPlayers(false, Set.copyOf(playerUuids));
        }

        public boolean allows(UUID playerUuid) {
            return unrestricted || playerUuids.contains(playerUuid);
        }
    }
}
