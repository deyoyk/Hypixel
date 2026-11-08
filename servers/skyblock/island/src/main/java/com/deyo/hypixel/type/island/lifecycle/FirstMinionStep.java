package com.deyo.hypixel.type.island.lifecycle;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributeMinionData;
import com.deyo.hypixel.type.generic.utility.MathUtility;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionRegistry;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class FirstMinionStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.CREATE;
    }

    @Override
    public int order() {
        return 20;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        MathUtility.delay(() -> {
            IslandMinionData.IslandMinion minion = context.island().getMinionData().initializeMinion(
                    new Pos(3, 100, 36),
                    MinionRegistry.COBBLESTONE,
                    new ItemAttributeMinionData.MinionData(1, 0),
                    false
            );

            context.island().getMinionData().spawn(minion);
        }, 40);
    }
}
