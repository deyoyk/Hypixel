package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.VanillaRandomStrollGoal;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.loottable.OtherLoot;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MobMinionCow extends SkyBlockMob {

    public MobMinionCow() {
        super(EntityType.COW);
    }

    @Override
    public String getDisplayName() {
        return "Cow";
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new VanillaRandomStrollGoal(this, 15)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return new ArrayList<>();
    }

    @Override
    public ItemStatistics getBaseStatistics() {
        return ItemStatistics.builder()
                .withBase(ItemStatistic.HEALTH, 100D)
                .withBase(ItemStatistic.SPEED, 70D)
                .build();
    }

    @Override
    public @Nullable SkyBlockLootTable getLootTable() {
        return new SkyBlockLootTable() {
            @Override
            public List<LootRecord> getLootTable() {
                return List.of(new LootRecord(ItemType.BEEF, 1, 100));
            }

            @Override
            public CalculationMode getCalculationMode() {
                return CalculationMode.CALCULATE_INDIVIDUAL;
            }
        };
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.FARMING;
    }

    @Override
    public long damageCooldown() {
        return 200;
    }

    @Override
    public OtherLoot getOtherLoot() {
        return new OtherLoot(0, 0, 5);
    }

    @Override
    public List<MobType> getMobTypes() {
        return List.of(MobType.ANIMAL);
    }
}

