package com.deyo.hypixel.type.skyblockgeneric.event.actions.npc;

import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.trait.NPCAbiphoneTrait;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.skyblockgeneric.abiphone.AbiphoneNPC;
import com.deyo.hypixel.type.skyblockgeneric.abiphone.AbiphoneRegistry;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.AbiphoneComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.tinylog.Logger;

public class ActionPlayerInteractNPC implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
	public void run(NPCInteractEvent event) {
		final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
		HypixelNPC npc = event.getNpc();
		SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());
		if (item.hasComponent(AbiphoneComponent.class)) {
			if (!(npc instanceof NPCAbiphoneTrait trait)) {
				player.sendMessage("<7>[<b>✆<7>] This NPC doesn't own an Abiphone...");
				event.setCancelled(true);
				return;
			}
			AbiphoneNPC abiphoneNPC = AbiphoneRegistry.getFromId(trait.getAbiphoneKey());
			if (abiphoneNPC != null) {
				if (item.getAttributeHandler().hasAbiphoneNPC(abiphoneNPC)) {
					player.sendMessage("<7>[<b>✆<7>] This NPC is already in your contacts list!");
					return;
				}
				abiphoneNPC.onAdd(player, player.getHeldSlot());
			} else {
				Logger.warn("NPC " + npc.getClass().getName() + " has an invalid Abiphone key: " + trait.getAbiphoneKey());
			}
			event.setCancelled(true);
		}
	}

}
