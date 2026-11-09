package com.deyo.hypixel.type.skyblockgeneric.user.island;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IslandMembersTest {

    @Test
    void soloIslandsDoNotFailWhenNoPlayerIsLoaded() {
        assertTrue(IslandMembers.onlineMembers(UUID.randomUUID(), null).isEmpty());
        assertTrue(IslandMembers.memberProfiles(UUID.randomUUID(), null).size() == 1);
    }
}
