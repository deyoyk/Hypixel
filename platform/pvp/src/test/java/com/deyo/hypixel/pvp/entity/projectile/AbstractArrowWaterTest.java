package com.deyo.hypixel.pvp.entity.projectile;

import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractArrowWaterTest {

    @Test
    void waterDragSlowsTheArrow() {
        Vec dragged = AbstractArrow.applyWaterDrag(new Vec(1, 2, -3));

        assertEquals(0.6, dragged.x(), 1e-9);
        assertEquals(1.2, dragged.y(), 1e-9);
        assertEquals(-1.8, dragged.z(), 1e-9);
    }
}
