package com.deyo.hypixel.type.island.lifecycle;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;
import com.deyo.hypixel.type.skyblockgeneric.utility.JerryInformation;

public class JerryDefaultsStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.CREATE;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        context.island().setJerryInformation(new JerryInformation(null, new Pos(9.5, 100, 33.5, 145, 0), null));
    }
}
