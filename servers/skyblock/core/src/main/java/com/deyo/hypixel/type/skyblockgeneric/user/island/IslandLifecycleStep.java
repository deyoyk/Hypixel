package com.deyo.hypixel.type.skyblockgeneric.user.island;

public interface IslandLifecycleStep {
    IslandLifecyclePhase phase();

    default int order() {
        return 0;
    }

    void run(IslandLifecycleContext context);
}
