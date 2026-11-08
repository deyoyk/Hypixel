package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionSkillHypixelLevel implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(SkillUpdateEvent event) {
        if (event.getNewValueRaw() <= event.getOldValueRaw()) return;

        SkyBlockPlayer player = event.getPlayer();
        SkillCategories skillCategory = event.getSkillCategory();

        int oldLevel = player.getSkills().getLevelAt(skillCategory, event.getOldValueRaw());
        int newLevel = player.getSkills().getLevelAt(skillCategory, event.getNewValueRaw());

        if (oldLevel == newLevel) return;

        for (int level = oldLevel + 1; level <= newLevel; level++) {
            player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getSkillCause(skillCategory, level));
        }
    }
}
