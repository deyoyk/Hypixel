package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.SkillUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockActionBar;

public class ActionSkillDisplay implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(SkillUpdateEvent event) {
        double oldValue = event.getOldValueRaw();
        double newValue = event.getNewValueRaw();
        double difference = newValue - oldValue;

        if (difference <= 0) return;

        int currentLevel = event.getPlayer().getSkills().getCurrentLevel(event.getSkillCategory());
        if (currentLevel >= event.getSkillCategory().asCategory().getHighestLevel()) return;
        int currentRequirement = event.getSkillCategory().asCategory().getReward(currentLevel + 1).requirement();

        SkyBlockActionBar.getFor(event.getPlayer()).addReplacement(
                SkyBlockActionBar.BarSection.DEFENSE,
                Text.of("<3>+{} {} ({}/{})", difference, event.getSkillCategory(),
                        event.getNewValueCumulative(), currentRequirement),
                20,
                2
        );
    }
}
