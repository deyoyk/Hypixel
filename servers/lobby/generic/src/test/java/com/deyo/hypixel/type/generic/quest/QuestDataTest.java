package com.deyo.hypixel.type.generic.quest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestDataTest {

    @Test
    void completedQuestCountTracksUniqueQuests() {
        QuestData data = new QuestData();

        data.completeQuest("quest-one");
        data.completeQuest("quest-two");
        data.completeQuest("quest-one");

        assertEquals(2, data.getCompletedQuestCount());
    }
}
