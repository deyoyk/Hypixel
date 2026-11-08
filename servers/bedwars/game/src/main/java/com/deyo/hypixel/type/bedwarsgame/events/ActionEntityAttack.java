package com.deyo.hypixel.type.bedwarsgame.events;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.entity.EntityDamageEvent;
import com.deyo.hypixel.pvp.events.PrepareAttackEvent;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.item.impl.LuckyCombatEffects;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.GameState;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class ActionEntityAttack implements HypixelEventClass {

	@PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false)
	public void run(PrepareAttackEvent event) {
		if (event.getEntity() instanceof BedWarsPlayer player) {
			BedWarsGame game = player.getGame();
			if (game == null) {
				event.setCancelled(true);
				return;
			}

			if (game.getState() != GameState.IN_PROGRESS) {
				event.setCancelled(true);
				return;
			}

			for (Entity entity : HypixelNPC.getPerPlayerNPCs().get(player.getUuid()).getEntityImpls().values()) {
				if (event.getTarget() == entity) {
					event.setCancelled(true);
					return;
				}
			}

			if (game.isSameTeam(player.getUuid(), event.getTarget().getUuid())) {
				player.sendMessage("<c>You cannot attack your teammate!");
				event.setCancelled(true);
				return;
			}

			LuckyCombatEffects.handle(player, event.getTarget());
		}
	}

	@PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
	public void run(EntityDamageEvent event) {
		if (event.getEntity() instanceof BedWarsPlayer player) {
			BedWarsGame game = player.getGame();
			if (game == null) {
				return;
			}

			if (game.getState() == GameState.IN_PROGRESS) {
				player.updateBelowTag();
			}
		}
	}

}
