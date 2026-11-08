package com.deyo.hypixel.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;

public interface BlockBreakable {
    void onBreak(PlayerBlockBreakEvent event, SkyBlockBlock block);
}
