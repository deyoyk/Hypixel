package com.deyo.hypixel.type.island.gui;

import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGuestManagementTest {

    @Test
    void onlyIslandMembersCanManageGuests() {
        UUID owner = UUID.randomUUID();
        UUID member = UUID.randomUUID();
        UUID guest = UUID.randomUUID();
        CoopDatabase.Coop coop = new CoopDatabase.Coop(
                UUID.randomUUID(), owner, List.of(owner, member), List.of(), List.of());

        assertTrue(GUIGuestManagement.canManage(owner, coop));
        assertTrue(GUIGuestManagement.canManage(member, coop));
        assertFalse(GUIGuestManagement.canManage(guest, coop));
        assertFalse(GUIGuestManagement.canManage(owner, null));
    }
}
