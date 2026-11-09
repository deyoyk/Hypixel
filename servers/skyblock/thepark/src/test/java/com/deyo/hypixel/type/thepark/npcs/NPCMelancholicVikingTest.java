package com.deyo.hypixel.type.thepark.npcs;

import org.junit.jupiter.api.Test;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NPCMelancholicVikingTest {

    @Test
    void dialogueSetsHandleANullPlayerAndExposeTheIntro() {
        NPCMelancholicViking viking = new NPCMelancholicViking();

        HypixelNPC.DialogueSet[] dialogues = viking.dialogues(null);

        assertTrue(Arrays.stream(dialogues)
                .anyMatch(dialogue -> dialogue.key().equals("intro")));
    }
}
