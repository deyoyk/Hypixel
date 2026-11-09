package com.deyo.hypixel.service.guild;

import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildPermission;
import com.deyo.hypixel.commons.guild.GuildRank;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildCacheRankPermissionTest {

    @Test
    void onlyTheGuildMasterCanEditRankPermissions() {
        UUID master = UUID.randomUUID();
        GuildData guild = guildWithCustomRank(master);
        GuildRank rank = guild.getRank("Custom");

        assertFalse(GuildCache.applyRankPermission(guild, UUID.randomUUID(), "Custom:KICK:true"));
        assertFalse(rank.hasPermission(GuildPermission.KICK));
        assertTrue(GuildCache.applyRankPermission(guild, master, "Custom:KICK:true"));
        assertTrue(rank.hasPermission(GuildPermission.KICK));
    }

    @Test
    void permissionsCanBeGrantedAndRevoked() {
        UUID master = UUID.randomUUID();
        GuildData guild = guildWithCustomRank(master);
        GuildRank rank = guild.getRank("Custom");

        assertTrue(GuildCache.applyRankPermission(guild, master, "Custom:INVITE:false"));
        assertFalse(rank.hasPermission(GuildPermission.INVITE));

        assertTrue(GuildCache.applyRankPermission(guild, master, "Custom:MUTE_MEMBERS:true"));
        assertTrue(rank.hasPermission(GuildPermission.MUTE_MEMBERS));
    }

    @Test
    void theGuildMasterRankIsNotEditable() {
        UUID master = UUID.randomUUID();
        GuildData guild = new GuildData(UUID.randomUUID(), "Test", master);

        assertFalse(GuildCache.applyRankPermission(guild, master, "Guild Master:KICK:false"));
        assertTrue(guild.getRank("Guild Master").hasPermission(GuildPermission.KICK));
    }

    @Test
    void malformedRequestsAreRejected() {
        UUID master = UUID.randomUUID();
        GuildData guild = guildWithCustomRank(master);

        assertFalse(GuildCache.applyRankPermission(guild, master, "Custom:KICK"));
        assertFalse(GuildCache.applyRankPermission(guild, master, "Missing:KICK:true"));
        assertFalse(GuildCache.applyRankPermission(guild, master, "Custom:NOT_A_PERMISSION:true"));
    }

    private static GuildData guildWithCustomRank(UUID master) {
        GuildData guild = new GuildData(UUID.randomUUID(), "Test", master);
        guild.getRanks().add(new GuildRank("Custom", 5, false, EnumSet.of(GuildPermission.INVITE)));
        return guild;
    }
}
