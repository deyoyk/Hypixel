package com.deyo.hypixel.type.island.gui;

import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIGuestManagementTest {

    @Test
    void onlyCoopMembersCanManageGuests() {
        UUID owner = UUID.randomUUID();
        UUID member = UUID.randomUUID();
        UUID guest = UUID.randomUUID();
        UUID islandId = UUID.randomUUID();
        CoopDatabase.Coop coop = new CoopDatabase.Coop(
                UUID.randomUUID(), owner, List.of(owner, member), List.of(), List.of());

        assertTrue(GUIGuestManagement.canManage(owner, UUID.randomUUID(), coop, islandId));
        assertTrue(GUIGuestManagement.canManage(member, UUID.randomUUID(), coop, islandId));
        assertFalse(GUIGuestManagement.canManage(guest, UUID.randomUUID(), coop, islandId));
    }

    @Test
    void soloIslandOwnerCanManageGuests() {
        UUID owner = UUID.randomUUID();
        UUID profileId = UUID.randomUUID();
        UUID islandId = profileId;

        assertTrue(GUIGuestManagement.canManage(owner, profileId, null, islandId));
    }

    @Test
    void soloIslandVisitorsCannotManageGuests() {
        UUID owner = UUID.randomUUID();
        UUID guest = UUID.randomUUID();
        UUID ownerProfile = UUID.randomUUID();
        UUID guestProfile = UUID.randomUUID();
        UUID islandId = ownerProfile;

        assertFalse(GUIGuestManagement.canManage(guest, guestProfile, null, islandId));
        assertFalse(GUIGuestManagement.canManage(owner, ownerProfile, null, null));
    }
}
