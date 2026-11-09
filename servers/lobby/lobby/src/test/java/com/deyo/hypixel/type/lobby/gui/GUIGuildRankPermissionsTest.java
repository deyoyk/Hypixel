package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.commons.guild.GuildPermission;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGuildRankPermissionsTest {

    @Test
    void togglingAddsAndRemovesPermissions() {
        Set<GuildPermission> initial = EnumSet.of(GuildPermission.INVITE);

        Set<GuildPermission> added = GUIGuildRankPermissions.toggled(
                initial, GuildPermission.KICK, true);
        assertTrue(added.contains(GuildPermission.INVITE));
        assertTrue(added.contains(GuildPermission.KICK));

        Set<GuildPermission> removed = GUIGuildRankPermissions.toggled(
                added, GuildPermission.INVITE, false);
        assertFalse(removed.contains(GuildPermission.INVITE));
        assertTrue(removed.contains(GuildPermission.KICK));
        assertEquals(Set.of(GuildPermission.INVITE), initial);
    }
}
