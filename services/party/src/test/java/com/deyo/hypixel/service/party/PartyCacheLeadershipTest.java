package com.deyo.hypixel.service.party;

import com.deyo.hypixel.commons.party.FullParty;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartyCacheLeadershipTest {

    @Test
    void onlyExistingMembersWithTheLeaderRoleAreLeaders() {
        UUID leader = UUID.randomUUID();
        FullParty party = FullParty.create(leader, UUID.randomUUID());

        assertTrue(PartyCache.isLeader(party, leader));
        assertFalse(PartyCache.isLeader(party, UUID.randomUUID()));
    }
}
