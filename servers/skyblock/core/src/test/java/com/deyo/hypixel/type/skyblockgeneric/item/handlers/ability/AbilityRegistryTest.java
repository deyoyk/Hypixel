package com.deyo.hypixel.type.skyblockgeneric.item.handlers.ability;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AbilityRegistryTest {

    @Test
    void nearestPositionUsesDistanceFromOrigin() {
        Pos origin = new Pos(0, 0, 0);
        Pos near = new Pos(2, 0, 0);
        Pos far = new Pos(10, 0, 0);

        assertEquals(near, AbilityRegistry.nearestPosition(origin, List.of(far, near)));
        assertNull(AbilityRegistry.nearestPosition(origin, List.of()));
    }
}
