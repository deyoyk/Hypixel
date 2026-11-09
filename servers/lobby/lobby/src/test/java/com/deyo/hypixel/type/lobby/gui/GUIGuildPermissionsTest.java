package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.commons.guild.GuildPermission;
import com.deyo.hypixel.commons.guild.GuildRank;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGuildPermissionsTest {

    @Test
    void rankPermissionsAreCopiedForTheEditor() {
        GuildRank rank = new GuildRank("Custom", 5, false,
                EnumSet.of(GuildPermission.INVITE, GuildPermission.KICK));

        var permissions = GUIGuildPermissions.permissionsOf(rank);

        assertTrue(permissions.contains(GuildPermission.INVITE));
        assertTrue(permissions.contains(GuildPermission.KICK));
        rank.setPermission(GuildPermission.INVITE, false);
        assertTrue(permissions.contains(GuildPermission.INVITE));
    }

    @Test
    void legacyRanksExposeAnEmptyPermissionSet() {
        GuildRank rank = new GuildRank("Legacy", 10, true, null);

        assertTrue(GUIGuildPermissions.permissionsOf(rank).isEmpty());
        assertFalse(rank.hasPermission(GuildPermission.INVITE));
    }
}
