package com.deyo.hypixel.type.island.lifecycle;

import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class MinionSaveStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.SAVE;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        IslandMinionData minionData = context.island().getMinionData();
        minionData.getMinions().forEach(IslandMinionData.IslandMinion::removeMinion);
        context.island().getDatabase().insertOrUpdate("minions", minionData.serialize());
    }
}
