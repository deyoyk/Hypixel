package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.skill;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActionSkillMiningHandlerTest {

    @Test
    void lapisArmorGrantsFiftyPercentMoreMiningExperience() {
        assertEquals(150.0, ActionSkillMiningHandler.applyMiningExperienceBonus(100.0, true), 1e-9);
    }

    @Test
    void otherSetupsDoNotChangeMiningExperience() {
        assertEquals(100.0, ActionSkillMiningHandler.applyMiningExperienceBonus(100.0, false), 1e-9);
    }
}
