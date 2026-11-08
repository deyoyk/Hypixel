package com.deyo.hypixel.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.type.AnvilInventory;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.HypixelAnvilGUI;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryCloseAnvil implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(InventoryCloseEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelAnvilGUI.anvilGUIs.containsKey(player)) {
            if (event.getInventory() instanceof AnvilInventory) {
                HypixelAnvilGUI.anvilGUIs.get(player).getValue().complete(HypixelAnvilGUI.anvilGUIs.get(player).getKey());
                HypixelAnvilGUI.anvilGUIs.remove(player);
            }
        }
    }
}

