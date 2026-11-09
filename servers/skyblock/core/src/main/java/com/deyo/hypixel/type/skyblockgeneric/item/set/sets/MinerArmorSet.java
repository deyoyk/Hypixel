package com.deyo.hypixel.type.skyblockgeneric.item.set.sets;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.set.impl.ArmorSet;
import com.deyo.hypixel.type.skyblockgeneric.item.set.impl.MuseumableSet;
import com.deyo.hypixel.type.skyblockgeneric.item.set.impl.SetEvents;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.statistics.TemporaryConditionalStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MinerArmorSet implements ArmorSet, SetEvents, MuseumableSet {
	private static final Map<UUID, TemporaryConditionalStatistic> ACTIVE_BONUSES = new ConcurrentHashMap<>();

	@Override
	public String getName() {
		return "Depth Coating";
	}

	@Override
	public ArrayList<String> getDescription() {
		return new ArrayList<>(List.of(
				"<f>Doubles this piece's <a>Defense <f>while on <a>Mining Islands<f>."
		));
	}

	@Override
	public void setPutOn(SkyBlockPlayer player) {
		removeBonus(player);

		TemporaryConditionalStatistic bonus = TemporaryConditionalStatistic.builder()
				.withStatistics(bonusPlayer -> isMiningIsland(HypixelConst.getTypeLoader().getType())
						? ItemStatistics.builder()
								.withBase(ItemStatistic.DEFENSE, armorDefense(bonusPlayer))
								.build()
						: ItemStatistics.empty())
				.withExpiry(this::isWearingSet)
				.build();

		ACTIVE_BONUSES.put(player.getUuid(), bonus);
		player.getStatistics().boostStatistic(bonus);
	}

	@Override
	public void setTakeOff(SkyBlockPlayer player) {
		removeBonus(player);
	}

	private void removeBonus(SkyBlockPlayer player) {
		TemporaryConditionalStatistic previous = ACTIVE_BONUSES.remove(player.getUuid());
		if (previous != null) {
			player.getStatistics().removeConditionalStatistic(previous);
		}
	}

	private static double armorDefense(SkyBlockPlayer player) {
		return defenseOf(player.getHelmet())
				+ defenseOf(player.getChestplate())
				+ defenseOf(player.getLeggings())
				+ defenseOf(player.getBoots());
	}

	private static double defenseOf(net.minestom.server.item.ItemStack item) {
		return new SkyBlockItem(item).getAttributeHandler().getStatistics().getOverall(ItemStatistic.DEFENSE);
	}

	static boolean isMiningIsland(ServerType type) {
		return type == ServerType.SKYBLOCK_GOLD_MINE
				|| type == ServerType.SKYBLOCK_DEEP_CAVERNS
				|| type == ServerType.SKYBLOCK_DWARVEN_MINES;
	}
}
