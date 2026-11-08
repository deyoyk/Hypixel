package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointSkills;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.SkillableMineComponent;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;

public class ActionSkillMiningHandler implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        SkyBlockItem item = new SkyBlockItem(event.getMaterial());

        if (!item.hasComponent(SkillableMineComponent.class)) return;

        SkillableMineComponent skillableMine = item.getComponent(SkillableMineComponent.class);
        SkillCategories skillCategory = skillableMine.getCategory();
        DatapointSkills.PlayerSkills skills = event.getPlayer().getSkills();

        skills.increase(event.getPlayer(), skillCategory, skillableMine.getMiningValue());
    }
}
