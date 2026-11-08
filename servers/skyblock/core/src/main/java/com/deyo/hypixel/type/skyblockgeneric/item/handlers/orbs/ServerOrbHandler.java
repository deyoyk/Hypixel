package com.deyo.hypixel.type.skyblockgeneric.item.handlers.orbs;

import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.entity.ServerCrystalImpl;

import java.util.function.Function;

public record ServerOrbHandler(
        Function<ServerCrystalImpl, Block> spawnMaterialFunction
) {
}