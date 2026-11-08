package com.deyo.hypixel.anticheat.prediction.modifier.impl;

import com.deyo.hypixel.anticheat.math.Vel;
import com.deyo.hypixel.anticheat.prediction.PlayerContext;
import com.deyo.hypixel.anticheat.prediction.modifier.VelocityModifier;

/**
 * Dolphin's Grace effect modifier
 * Greatly increases swim speed
 */
public class DolphinsGraceModifier extends VelocityModifier {

    private static final double DOLPHINS_GRACE_MULTIPLIER = 1.5;

    @Override
    public Vel apply(Vel currentVel, PlayerContext context) {
        return new Vel(
            currentVel.x() * DOLPHINS_GRACE_MULTIPLIER,
            currentVel.y() * DOLPHINS_GRACE_MULTIPLIER,
            currentVel.z() * DOLPHINS_GRACE_MULTIPLIER
        );
    }

    @Override
    public boolean shouldApply(PlayerContext context) {
        return context.isHasDolphinsGrace() && context.isInWater();
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public String getName() {
        return "Dolphin's Grace";
    }
}
