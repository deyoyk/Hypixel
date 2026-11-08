package com.deyo.hypixel.type.island.lifecycle;

import com.deyo.hypixel.type.skyblockgeneric.structure.structures.IslandPortal;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class PortalBuildStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.LOAD;
    }

    @Override
    public int order() {
        return 20;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        IslandPortal portal = new IslandPortal(0, 6, 100, 43);
        portal.setType(IslandPortal.PortalType.HUB);
        portal.build(context.island().getIslandInstance());
    }
}
