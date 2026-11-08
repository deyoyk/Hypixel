package com.deyo.hypixel.type.skyblockgeneric.block.impl;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;

public interface BlockInteractable {
    void onInteract(PlayerBlockInteractEvent event, SkyBlockBlock block);
}
