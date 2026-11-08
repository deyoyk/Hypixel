package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;
import com.deyo.hypixel.type.skyblockgeneric.block.impl.BlockBreakable;

public class ActionBlockDestroy implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onDestroy(PlayerBlockBreakEvent event) {
        if (SkyBlockBlock.isSkyBlockBlock(event.getBlock())) {
            SkyBlockBlock block = new SkyBlockBlock(event.getBlock());
            if (block.getGenericInstance() instanceof BlockBreakable breakable) {
                breakable.onBreak(event, block);
            }
        }
    }
}
