package com.deyo.hypixel.type.murdermysterylobby.npcs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NPCInfectionTest {

    @Test
    void infectionUsesItsOwnUnavailableMessage() {
        assertEquals("<7>Infection is coming soon...", NPCInfection.UNAVAILABLE_MESSAGE);
        assertFalse(NPCInfection.UNAVAILABLE_MESSAGE.toLowerCase().contains("not implemented"));
    }
}
