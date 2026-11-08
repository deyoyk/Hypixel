package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.VanillaMeleeAttackGoal;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai.VanillaRandomStrollGoal;
import com.deyo.hypixel.type.skyblockgeneric.loottable.OtherLoot;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobMinionZombie extends SkyBlockMob {

    public MobMinionZombie() {
        super(EntityType.ZOMBIE);
    }

    @Override
    public String getDisplayName() {
        return "Zombie";
    }

    @Override
    public Integer getLevel() {
        return 1;
    }

    @Override
    public List<GoalSelector> getGoalSelectors() {
        return List.of(
                new VanillaMeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK),
                new VanillaRandomStrollGoal(this, 15)
        );
    }

    @Override
    public List<TargetSelector> getTargetSelectors() {
        return List.of(
                new LastEntityDamagerTarget(this, 16), // First target the last entity which attacked you
                new ClosestEntityTarget(this, 16, entity -> entity instanceof SkyBlockPlayer)
        );
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
        return null;
    }

    @Override
    public SkillCategories getSkillCategory() {
        return SkillCategories.COMBAT;
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
        return List.of(MobType.UNDEAD);
    }
}
