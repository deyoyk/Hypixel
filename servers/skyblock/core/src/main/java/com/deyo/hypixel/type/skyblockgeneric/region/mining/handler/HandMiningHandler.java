package com.deyo.hypixel.type.skyblockgeneric.region.mining.handler;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.List;

/**
 * Mining handler for blocks that can be broken by any tool or by hand.
 */
public class HandMiningHandler implements SkyBlockMiningHandler {
    private final double strength;

    public HandMiningHandler(double strength) {
        this.strength = strength;
    }

    @Override
    public List<Class<? extends SkyBlockItemComponent>> getValidToolComponents() {
        return List.of();
    }

    @Override
    public double getStrength() {
        return strength;
    }

    @Override
    public int getMiningPowerRequirement() {
        return 0;
    }

    @Override
    public String getHandlerName() {
        return "Hand";
    }
}
