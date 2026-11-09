package com.deyo.hypixel.commons.guild;

import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildRankTest {

    @Test
    void missingPermissionsAreTreatedAsNoPermissions() {
        GuildRank rank = new GuildRank("Legacy", 10, true, null);

        assertFalse(rank.hasPermission(GuildPermission.INVITE));
    }

    @Test
    void permissionsAreCheckedByMembership() {
        GuildRank rank = new GuildRank("Officer", 1, true, EnumSet.of(GuildPermission.KICK));

        assertTrue(rank.hasPermission(GuildPermission.KICK));
        assertFalse(rank.hasPermission(GuildPermission.INVITE));
    }

    @Test
    void lowerPriorityIsHigherRank() {
        assertTrue(GuildRank.guildMaster().isHigherThan(GuildRank.officer()));
        assertFalse(GuildRank.member().isHigherThan(GuildRank.officer()));
    }
}
