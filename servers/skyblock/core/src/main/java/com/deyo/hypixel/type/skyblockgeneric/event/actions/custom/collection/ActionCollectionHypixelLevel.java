package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.collection;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategories;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategory;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CollectionUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;

public class ActionCollectionHypixelLevel implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(CollectionUpdateEvent event) {
        if (CollectionCategories.getCategory(event.getItemType()) == null) return;

        CollectionCategory.ItemCollection collection = CollectionCategories.getCategory(event.getItemType()).getCollection(event.getItemType());
        int newValue = event.getPlayer().getCollection().get(event.getItemType());
        for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
            if (event.getOldValue() < reward.requirement() && newValue >= reward.requirement()) {
                event.getPlayer().getSkyBlockExperience().addExperience(
                        SkyBlockLevelCause.getCollectionCause(event.getItemType(), collection.getPlacementOf(reward))
                );
            }
        }
    }
}
