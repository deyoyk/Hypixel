package com.deyo.hypixel.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryClose implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(InventoryCloseEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
			HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

			if (gui == null) return;

			gui.onClose(event, HypixelInventoryGUI.CloseReason.PLAYER_EXITED);
			HypixelInventoryGUI.GUI_MAP.remove(player.getUuid());
		}
	}
}
