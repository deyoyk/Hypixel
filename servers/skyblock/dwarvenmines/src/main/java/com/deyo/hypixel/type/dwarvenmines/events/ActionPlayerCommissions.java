package com.deyo.hypixel.type.dwarvenmines.events;

import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.dwarvenmines.commission.Commission;
import com.deyo.hypixel.type.dwarvenmines.commission.Commissions;
import com.deyo.hypixel.type.dwarvenmines.commission.Objective;
import com.deyo.hypixel.type.dwarvenmines.commission.ObjectiveType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointCommissions;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Set;

public class ActionPlayerCommissions implements HypixelEventClass {

	private static final Set<Material> MITHRIL_BLOCKS = Set.of(Material.LIGHT_BLUE_WOOL, Material.GRAY_WOOL, Material.CYAN_TERRACOTTA, Material.PRISMARINE, Material.DARK_PRISMARINE, Material.PRISMARINE_BRICKS);

	private static final Set<Material> TITANIUM_BLOCKS = Set.of(Material.POLISHED_DIORITE);

	@PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
	public void run(PlayerBlockBreakEvent event) {
		if (!(event.getPlayer() instanceof SkyBlockPlayer player)) return;

		Material brokenMaterial = event.getBlock().material();
		if (brokenMaterial == null) return;

		Objective.BlockTarget target = null;
		if (MITHRIL_BLOCKS.contains(brokenMaterial)) {
			target = Objective.BlockTarget.MITHRIL;
		} else if (TITANIUM_BLOCKS.contains(brokenMaterial)) {
			target = Objective.BlockTarget.TITANIUM;
		}

		if (target == null) return;

		SkyBlockRegion region = player.getRegion();
		RegionType regionType = region != null ? region.getType() : null;

		updateCommissionProgress(player, ObjectiveType.MINE, target, 1, regionType);
	}

	public static void updateCommissionProgress(SkyBlockPlayer player, ObjectiveType type, Objective.BlockTarget target, int amount, RegionType regionType) {
		SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
		DatapointCommissions.PlayerCommissionData commissionData = dataHandler.get(SkyBlockDataHandler.Data.COMMISSIONS, DatapointCommissions.class).getValue();

		boolean anyUpdated = false;

		for (DatapointCommissions.ActiveCommission activeCommission : commissionData.getActiveCommissions()) {
			if (activeCommission.isCompleted()) continue;

			Commission commission = Commissions.getCommissionByName(activeCommission.getCommissionName());
			if (commission == null) continue;

			// Check if this commission matches the action
			if (commission.objective.type != type) continue;
			if (commission.objective.target != target) continue;

			if (!commission.objective.location.isAny() && regionType != null) {
				if (!commission.objective.location.allows(regionType)) continue;
			}

			int newProgress = activeCommission.getProgress() + amount;
			activeCommission.setProgress(newProgress);
			anyUpdated = true;

			if (newProgress >= commission.objective.amount) {
				activeCommission.setCompleted(true);
				onCommissionComplete(player, commission);
			}
		}

		if (anyUpdated) {
			Commissions.saveCommissionData(player, commissionData);
		}
	}

	private static void onCommissionComplete(SkyBlockPlayer player, Commission commission) {
		player.sendMessage("<a><l>{} </l><e>Commission Complete! Visit the King to claim your rewards!", commission.name);
	}
}
