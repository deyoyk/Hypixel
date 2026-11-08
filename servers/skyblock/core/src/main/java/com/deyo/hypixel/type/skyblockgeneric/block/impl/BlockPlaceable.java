package com.deyo.hypixel.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;

public interface BlockPlaceable {

    void onPlace(PlayerBlockPlaceEvent event, SkyBlockBlock block);
}
