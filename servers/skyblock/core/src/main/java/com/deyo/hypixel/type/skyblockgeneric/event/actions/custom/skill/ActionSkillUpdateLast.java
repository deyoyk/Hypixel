package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointSkillCategory;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.SkillUpdateEvent;

public class ActionSkillUpdateLast implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(SkillUpdateEvent event) {
        double oldValue = event.getOldValueRaw();
        double newValue = event.getNewValueRaw();
        double difference = newValue - oldValue;

        if (difference <= 0) return;
        event.getPlayer().getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.LAST_EDITED_SKILL, DatapointSkillCategory.class)
                .setValue(event.getSkillCategory());
    }
}

