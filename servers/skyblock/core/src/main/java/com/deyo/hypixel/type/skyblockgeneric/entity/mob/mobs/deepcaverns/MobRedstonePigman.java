package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.deepcaverns;

import lombok.NonNull;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIMaterial;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.ClosestEntityRegionTarget;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.MeleeAttackWithinRegionGoal;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.RandomRegionStrollGoal;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.impl.RegionPopulator;
import com.deyo.hypixel.type.skyblockgeneric.loottable.OtherLoot;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MobRedstonePigman extends BestiaryMob implements RegionPopulator {

	private static final long ORE_BREAKER_MEMORY_MS = 30_000;
	private static final Map<UUID, Long> ORE_BREAKERS = new ConcurrentHashMap<>();

	public MobRedstonePigman() {
		super(EntityType.ZOMBIFIED_PIGLIN);
	}

	public static void markOreBreaker(UUID playerUuid) {
		markOreBreaker(playerUuid, System.currentTimeMillis());
	}

	static void markOreBreaker(UUID playerUuid, long now) {
		ORE_BREAKERS.put(playerUuid, now);
	}

	public static boolean isOreBreaker(UUID playerUuid) {
		return isOreBreaker(playerUuid, System.currentTimeMillis());
	}

	static boolean isOreBreaker(UUID playerUuid, long now) {
		Long brokenAt = ORE_BREAKERS.get(playerUuid);
		if (brokenAt == null) {
			return false;
		}
		if (now - brokenAt >= ORE_BREAKER_MEMORY_MS) {
			ORE_BREAKERS.remove(playerUuid, brokenAt);
			return false;
		}
		return true;
	}

	static void clearOreBreakers() {
		ORE_BREAKERS.clear();
	}

	@Override
	public String getDisplayName() {
		return "Redstone Pigman";
	}

	@Override
	public Integer getLevel() {
		return 3;
	}

	@Override
	public List<GoalSelector> getGoalSelectors() {
		return List.of(
				new MeleeAttackWithinRegionGoal(this,
						1.6,
						20,
						TimeUnit.SERVER_TICK,
						RegionType.PIGMENS_DEN),
				new RandomRegionStrollGoal(this, 15, RegionType.PIGMENS_DEN)  // Walk around
		);
	}

	@Override
	public List<TargetSelector> getTargetSelectors() {
		return List.of(
				new LastEntityDamagerTarget(this, 12), // First target the last entity which attacked you
				new ClosestEntityRegionTarget(this,
						6,
						entity -> entity instanceof SkyBlockPlayer player && isOreBreaker(player.getUuid()),
						RegionType.PIGMENS_DEN)
		);
	}

	@Override
	public ItemStatistics getBaseStatistics() {
		return ItemStatistics.builder()
				.withBase(ItemStatistic.HEALTH, 250D)
				.withBase(ItemStatistic.DAMAGE, 75D)
				.withBase(ItemStatistic.SPEED, 100D)
				.build();
	}

	@Override
	public @Nullable SkyBlockLootTable getLootTable() {
		return new SkyBlockLootTable() {
			@Override
			public @NonNull List<LootRecord> getLootTable() {
				return List.of(
						new LootRecord(ItemType.GOLD_NUGGET, 2, 50),
						new LootRecord(ItemType.REDSTONE, 5, 50)
						//new LootRecord(ItemType.FLAMING_SWORD, 1, 0.01),
						//new LootRecord(ItemType.EXP_SHARE_CORE, 1, 0.01)
				);
			}

			@Override
			public @NotNull CalculationMode getCalculationMode() {
				return CalculationMode.CALCULATE_INDIVIDUAL;
			}
		};
	}

	@Override
	public SkillCategories getSkillCategory() {
		return SkillCategories.COMBAT;
	}

	@Override
	public long damageCooldown() {
		return 500;
	}

	@Override
	public OtherLoot getOtherLoot() {
		return new OtherLoot(20, 12, 25);
	}

	@Override
	public List<MobType> getMobTypes() {
		return List.of(MobType.UNDEAD);
	}

	@Override
	public int getMaxBestiaryTier() {
		return 10;
	}

	@Override
	public int getBestiaryBracket() {
		return 1;
	}

	@Override
	public String getMobID() {
		return "REDSTONE_PIGMAN";
	}

	@Override
	public GUIMaterial getGuiMaterial() {
		return new GUIMaterial("74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb");
	}

	@Override
	public List<Populator> getPopulators() {
		return List.of(new Populator(RegionType.PIGMENS_DEN, 20));
	}
}
