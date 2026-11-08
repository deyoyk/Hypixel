package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import net.minestom.server.entity.attribute.Attribute;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionChangeMiningRegion implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (HypixelConst.isIslandServer()) return;

        player.getAttribute(Attribute.MINING_EFFICIENCY).setBaseValue(0D);
    }
}
