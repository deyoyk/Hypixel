package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.skyblockgeneric.event.actions.player.ActionPlayerChangeHypixelMenuDisplay;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionDisplayMenu implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(HypixelInventoryGUI.InventoryGUIOpenEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.player();
        ActionPlayerChangeHypixelMenuDisplay.setMainMenu(player);
    }
}
