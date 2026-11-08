package com.deyo.hypixel.type.generic.event.actions.gui;

import net.minestom.server.event.inventory.InventoryClickEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerInventoryPostClick implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(InventoryClickEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (HypixelInventoryGUI.GUI_MAP.containsKey(player.getUuid())) {
			HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(player.getUuid());

			if (gui == null) return;

			int slot = event.getSlot();
			GUIItem item = gui.get(slot);

			if (item == null) return;

			if (item instanceof GUIClickableItem clickable) {
				clickable.runPost(event, player);
			}
		}
	}
}
