package com.deyo.hypixel.type.island.tab;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IslandGuestsModuleTest {

    @Test
    void excludesIslandMembersFromGuests() {
        UUID owner = UUID.randomUUID();
        UUID guestOne = UUID.randomUUID();
        UUID guestTwo = UUID.randomUUID();

        assertEquals(
                List.of(guestOne, guestTwo),
                IslandGuestsModule.guestIds(
                        List.of(owner, guestOne, guestTwo),
                        List.of(owner))
        );
    }

    @Test
    void membersOnlyProduceNoGuests() {
        UUID owner = UUID.randomUUID();
        UUID member = UUID.randomUUID();

        assertTrue(IslandGuestsModule.guestIds(List.of(owner, member), List.of(owner, member)).isEmpty());
    }
}
