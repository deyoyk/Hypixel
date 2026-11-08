package com.deyo.hypixel.type.skyblockgeneric.block.attribute;

import com.deyo.hypixel.type.skyblockgeneric.block.BlockType;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;
import com.deyo.hypixel.type.skyblockgeneric.block.attribute.attributes.BlockAttributeType;

public class BlockAttributeHandler {
    SkyBlockBlock block;

    public BlockAttributeHandler(SkyBlockBlock block) {
        this.block = block;
    }

    public BlockType getBlockType() {
        return BlockType.valueOf(((BlockAttributeType) block.getAttribute("block_type")).getValue());
    }

}
