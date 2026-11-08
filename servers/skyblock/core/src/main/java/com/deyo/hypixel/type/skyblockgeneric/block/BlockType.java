package com.deyo.hypixel.type.skyblockgeneric.block;

import com.deyo.hypixel.type.skyblockgeneric.block.blocks.BlockBrewingStand;
import com.deyo.hypixel.type.skyblockgeneric.block.blocks.BlockChest;
import com.deyo.hypixel.type.skyblockgeneric.block.blocks.BlockDecoration;
import com.deyo.hypixel.type.skyblockgeneric.block.impl.CustomSkyBlockBlock;

public enum BlockType {
    CHEST(BlockChest.class),
    DECORATION(BlockDecoration.class),
    BREWING_STAND(BlockBrewingStand.class),
    ;

    public final Class<? extends CustomSkyBlockBlock> clazz;

    BlockType(Class<? extends CustomSkyBlockBlock> clazz) {
        this.clazz = clazz;
    }

    public static BlockType getFromName(String name) {
        for (BlockType blockType : values()) {
            if (blockType.name().equalsIgnoreCase(name)) {
                return blockType;
            }
        }
        return null;
    }
}
