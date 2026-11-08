package com.deyo.hypixel.type.generic.gui.v2.event;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.v2.ViewNavigator;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionInventoryPreClick implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.INVENTORY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onActionPlayerInventoryPreClick(InventoryPreClickEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ViewNavigator.find(player).ifPresent(navigator -> {
            if (navigator.getCurrentSession() == null) {
                return;
            }
            navigator.getCurrentSession().onPreClickEvent(event);
        });
    }

}
