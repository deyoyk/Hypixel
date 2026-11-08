package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickItemUpdate implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(InventoryItemChangeEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getInventory().getViewers().stream().toList().getFirst();

        if (!SkyBlockItem.isSkyBlockItem(event.getNewItem())) {
            player.getInventory().setItemStack(event.getSlot(), PlayerItemUpdater.playerUpdate(
                    player, event.getNewItem()).build());
        }
    }
}

