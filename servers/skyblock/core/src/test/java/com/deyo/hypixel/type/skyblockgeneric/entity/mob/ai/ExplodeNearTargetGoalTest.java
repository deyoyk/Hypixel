package com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExplodeNearTargetGoalTest {

    @Test
    void fuseCompletesAfterTheConfiguredDelay() {
        assertFalse(ExplodeNearTargetGoal.fuseComplete(1_000L, 2_499L));
        assertTrue(ExplodeNearTargetGoal.fuseComplete(1_000L, 2_500L));
    }

    @Test
    void explosionRangeIsInclusive() {
        assertTrue(ExplodeNearTargetGoal.isWithinExplosionRange(6.25));
        assertFalse(ExplodeNearTargetGoal.isWithinExplosionRange(6.26));
    }
}
