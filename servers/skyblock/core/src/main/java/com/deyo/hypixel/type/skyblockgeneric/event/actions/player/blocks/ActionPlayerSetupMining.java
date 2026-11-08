package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPlayerSetupMining implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onSpawn(PlayerSpawnEvent event) {
        var player = event.getPlayer();

        if (HypixelConst.isIslandServer()) return;

        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).clearModifiers();
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(0D);
    }
}
