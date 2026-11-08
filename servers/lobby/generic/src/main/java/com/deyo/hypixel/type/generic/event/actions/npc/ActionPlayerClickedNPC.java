package com.deyo.hypixel.type.generic.event.actions.npc;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.impl.NPCAnimalEntityImpl;
import com.deyo.hypixel.type.generic.entity.npc.impl.NPCEntityImpl;
import com.deyo.hypixel.type.generic.entity.npc.impl.NPCVillagerEntityImpl;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerClickedNPC implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerEntityInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

		if (event.getHand() != PlayerHand.MAIN) return;

		Entity entity = event.getTarget();
		HypixelNPC npc = HypixelNPC.getFromImpl(player, entity);
		if (npc == null) return;

		NPCInteractEvent clickEvent = new NPCInteractEvent(player, event.getHand(), npc);
		HypixelEventHandler.callCustomEvent(clickEvent);
		if (clickEvent.isCancelled()) return;

		switch (entity) {
			case NPCEntityImpl _, NPCVillagerEntityImpl _, NPCAnimalEntityImpl _ -> npc.onClick(clickEvent);
			default -> {
				// This is not a NPC we can handle here
				Logger.warn("Player " + player.getUsername() + " clicked on an unknown NPC type: " + entity.getClass().getName());
			}
		}
	}
}
