package com.deyo.hypixel.type.island.lifecycle;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.utility.MathUtility;
import com.deyo.hypixel.type.skyblockgeneric.block.placement.states.state.Facing;
import com.deyo.hypixel.type.skyblockgeneric.chest.ChestBuilder;
import com.deyo.hypixel.type.skyblockgeneric.chest.ChestType;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;

public class StarterChestStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.CREATE;
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        MathUtility.delay(() -> new ChestBuilder(context.island().getIslandInstance(), new Pos(10, 93, 38), ChestType.SINGLE, Facing.WEST)
                .setItem(0, ItemStack.of(Material.DIRT, 5))
                .setItem(1, ItemStack.of(Material.GRASS_BLOCK, 7))
                .setItem(2, ItemStack.of(Material.COBBLESTONE, 8))
                .setItem(3, ItemStack.of(Material.WATER_BUCKET))
                .setItem(4, ItemStack.of(Material.LAVA_BUCKET))
                .setItem(5, ItemStack.of(Material.BONE_MEAL, 3))
                .build(), 60);
    }
}
