package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.slayer;

import java.util.List;
import java.util.Optional;
import net.minestom.server.entity.EntityType;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.loottable.OtherLoot;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerTierDefinition;
import com.deyo.hypixel.type.skyblockgeneric.slayer.SlayerType;

public record SlayerBossProfile(
    SlayerType type,
    SlayerTierDefinition tier,
    List<MobType> mobTypes,
    EntityType entityType
) {
    public String id() {
        return type.name() + "_" + tier.tier().name();
    }

    public String displayName() {
        return type.displayName() + " " + tier.tier().numeral();
    }

    public ItemStatistics asBaseStatistics() {
        return ItemStatistics.builder()
            .withBase(ItemStatistic.HEALTH, tier.bossHealth())
            .withBase(ItemStatistic.DAMAGE, tier.bossDamage())
            .withBase(ItemStatistic.SPEED, tier.bossSpeed())
            .build();
    }

    public OtherLoot asOtherLoot() {
        return new OtherLoot(tier.requiredCombatXp(), 0, Math.max(1, tier.slayerXp() / 5));
    }

    public SkyBlockLootTable asLootTable() {
        Optional<ItemType> item = tier.tokenItem();
        if (item.isEmpty() || tier.tokenDrops() <= 0) {
            return null;
        }

        return new SkyBlockLootTable() {
            @Override
            public List<LootRecord> getLootTable() {
                return List.of(new LootRecord(item.get(), tier.tokenDrops(), 100));
            }

            @Override
            public CalculationMode getCalculationMode() {
                return CalculationMode.CALCULATE_INDIVIDUAL;
            }
        };
    }
}
