package com.deyo.hypixel.service.party;

import com.deyo.hypixel.commons.party.PendingParty;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyInviteExpiryTest {

    @Test
    void anOldInviteTimerCannotRemoveANewerInvite() {
        UUID invitee = UUID.randomUUID();
        UUID leader = UUID.randomUUID();
        PendingParty oldInvite = PendingParty.create(invitee, leader);
        PendingParty newInvite = PendingParty.create(invitee, leader);

        Map<UUID, PendingParty> invites = new ConcurrentHashMap<>();
        invites.put(invitee, newInvite);

        assertFalse(invites.remove(invitee, oldInvite));
        assertSame(newInvite, invites.get(invitee));

        assertTrue(invites.remove(invitee, newInvite));
    }
}
