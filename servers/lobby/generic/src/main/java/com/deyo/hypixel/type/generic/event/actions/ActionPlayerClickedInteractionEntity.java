package com.deyo.hypixel.type.generic.event.actions;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.entity.InteractionEntity;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerClickedInteractionEntity implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerEntityInteractEvent event) {
		final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
		if (event.getHand() != PlayerHand.MAIN) return;
		if (event.getTarget() instanceof InteractionEntity interaction) {
			interaction.onClick.accept(player, event);
		}
	}
}
