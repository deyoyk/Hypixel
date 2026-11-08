package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.collection;

import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategories;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategory;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointCollection;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CollectionUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockActionBar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionCollectionAdd implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(CustomBlockBreakEvent event) {
        if (event.getPlayerPlaced()) return;

        SkyBlockPlayer player = event.getPlayer();

        // Process each dropped item for collection
        for (com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem drop : event.getDrops()) {
            ItemType type = drop.getAttributeHandler().getPotentialType();
            if (type == null) continue;

            int oldAmount = player.getCollection().get(type);
            int dropAmount = drop.getAmount();
            player.getCollection().increase(type, dropAmount);

            player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.COLLECTION, DatapointCollection.class).setValue(
                    player.getCollection()
            );

            HypixelEventHandler.callCustomEvent(new CollectionUpdateEvent(player, type, oldAmount));

            CollectionCategory category = CollectionCategories.getCategory(type);
            if (category == null) continue;
            CollectionCategory.ItemCollection collection = category.getCollection(type);

            final int finalDropAmount = dropAmount;
            final ItemType finalType = type;
            ScheduleUtility.delay(() -> {
                SkyBlockActionBar bar = SkyBlockActionBar.getFor(player);
                int startingPriority = 5;
                int addedAmount = finalDropAmount;

                SkyBlockActionBar.DisplayReplacement existingReplacement = bar.getReplacement(SkyBlockActionBar.BarSection.DEFENSE);
                if (existingReplacement != null) {
                    startingPriority = existingReplacement.priority() + 1;
                    String plain = existingReplacement.display().plain();
                    int separator = plain.indexOf(' ');
                    if (separator > 0) {
                        try {
                            addedAmount = Integer.parseInt(plain.substring(0, separator)) + finalDropAmount;
                        } catch (NumberFormatException ignored) {}
                    }
                }
                if (player.getCollection().getReward(collection) != null) {
                    bar.addReplacement(
                            SkyBlockActionBar.BarSection.DEFENSE,
                            Text.of("<2>+{} {} <7>({}/{})",
                                    addedAmount,
                                    finalType.getDisplayName(),
                                    StringUtility.commaify(player.getCollection().get(finalType)),
                                    StringUtility.shortenNumber(player.getCollection().getReward(collection).requirement())),
                            20,
                            startingPriority
                    );
                } else { //if Collection is maxed
                    bar.addReplacement(
                            SkyBlockActionBar.BarSection.DEFENSE,
                            Text.of("<2>+{} {} <7>({})",
                                    addedAmount,
                                    finalType.getDisplayName(),
                                    StringUtility.commaify(player.getCollection().get(finalType))),
                            20,
                            startingPriority
                    );
                }
            }, 5);
        }
    }
}
