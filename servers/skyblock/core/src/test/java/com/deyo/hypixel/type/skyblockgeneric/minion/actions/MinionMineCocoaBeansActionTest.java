package com.deyo.hypixel.type.skyblockgeneric.minion.actions;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinionMineCocoaBeansActionTest {

    @Test
    void closestLogUsesActualDistance() {
        Pos target = new Pos(0, 0, 0);
        Pos near = new Pos(1, 0, 0);
        Pos medium = new Pos(3, 0, 0);
        Pos far = new Pos(10, 0, 0);

        assertEquals(near, MinionMineCocoaBeansAction.closestLog(List.of(far, medium, near), target));
    }

    @Test
    void closestLogReturnsNullWithoutCandidates() {
        assertNull(MinionMineCocoaBeansAction.closestLog(List.of(), new Pos(0, 0, 0)));
    }
}
