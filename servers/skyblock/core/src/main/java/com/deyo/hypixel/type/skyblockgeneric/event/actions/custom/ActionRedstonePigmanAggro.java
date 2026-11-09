package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.deepcaverns.MobRedstonePigman;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;

public class ActionRedstonePigmanAggro implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(CustomBlockBreakEvent event) {
        if (event.getMaterial() != Material.REDSTONE_ORE) {
            return;
        }

        SkyBlockRegion region = SkyBlockRegion.getRegionOfEntity(event.getPlayer());
        if (region == null || region.getType() != RegionType.PIGMENS_DEN) {
            return;
        }

        MobRedstonePigman.markOreBreaker(event.getPlayer().getUuid());
    }
}
