package com.deyo.hypixel.type.skyblockgeneric.region.mining.configurations;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.data.crystals.CrystalCatalog;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegenConfiguration;
import com.deyo.hypixel.type.skyblockgeneric.utility.groups.Groups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WheatAndFlowersConfiguration extends SkyBlockRegenConfiguration {
    @Override
    public MiningTask handleStageOne(MiningTask task, Pos brokenBlock) {
        task.setIntermediaryBlock(Block.AIR);
        task.setReviveBlock(Block.AIR);

        if (task.getInitialMinedBlock().equals(Block.WHEAT)) {
            task.setReviveBlock(Block.WHEAT);
        }

        return task;
    }

    @Override
    public MiningTask handleStageTwo(MiningTask task, Pos brokenBlock) {
        return MiningTask.never();
    }

    @Override
    public List<Material> getMineableBlocks(Instance instance, Point point) {
        ArrayList<Material> materials = new ArrayList<>(Collections.singletonList(Material.WHEAT));

        if (CrystalCatalog.getFromAround(HypixelConst.getTypeLoader().getType(), point.asPos(), 15)
                .stream().anyMatch(crystal -> crystal.itemType == ItemType.FLOWER_CRYSTAL)) {
            materials.addAll(new ArrayList<>(Groups.FLOWERS));
        }

        return materials;
    }

    @Override
    public long getRegenerationTime() {
        return 10000;
    }
}
