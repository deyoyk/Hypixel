package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.PlayerInventory;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.PlayerInventoryCrafting;

public class ActionPlayerCloseVanillaCrafting implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true)
    public void run(InventoryCloseEvent event) {
        if (!(event.getInventory() instanceof PlayerInventory)) {
            return;
        }

        PlayerInventoryCrafting.returnCraftingGrid((SkyBlockPlayer) event.getPlayer());
    }
}
