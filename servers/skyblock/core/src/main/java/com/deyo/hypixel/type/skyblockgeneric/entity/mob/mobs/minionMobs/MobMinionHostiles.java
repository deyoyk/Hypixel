package com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.utils.time.TimeUnit;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class MobMinionHostiles {
    private MobMinionHostiles() {
    }

    public abstract static class Hostile extends SkyBlockMob {
        private final String displayName;
        private final ItemType lootItem;

        protected Hostile(EntityType entityType, String displayName, ItemType lootItem, MobType mobType) {
            super(entityType, mobType);
            this.displayName = displayName;
            this.lootItem = lootItem;
        }

        @Override
        public String getDisplayName() {
            return displayName;
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
                    new LastEntityDamagerTarget(this, 16),
                    new ClosestEntityTarget(this, 16, entity -> entity instanceof SkyBlockPlayer)
            );
        }

        @Override
        public ItemStatistics getBaseStatistics() {
            return ItemStatistics.builder()
                    .withBase(ItemStatistic.HEALTH, 100D)
                    .withBase(ItemStatistic.DAMAGE, 20D)
                    .withBase(ItemStatistic.SPEED, 70D)
                    .build();
        }

        @Override
        public @Nullable SkyBlockLootTable getLootTable() {
            return new SkyBlockLootTable() {
                @Override
                public @NotNull List<LootRecord> getLootTable() {
                    return List.of(new LootRecord(lootItem, 1, 100));
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
            return new OtherLoot(0, 0, 5);
        }
    }

    public static final class Blaze extends Hostile {
        public Blaze() {
            super(EntityType.BLAZE, "Blaze", ItemType.BLAZE_ROD, MobType.INFERNAL);
        }
    }

    public static final class CaveSpider extends Hostile {
        public CaveSpider() {
            super(EntityType.CAVE_SPIDER, "Cave Spider", ItemType.SPIDER_EYE, MobType.ARTHROPOD);
        }
    }

    public static final class Creeper extends Hostile {
        public Creeper() {
            super(EntityType.CREEPER, "Creeper", ItemType.GUNPOWDER, MobType.CUBIC);
        }
    }

    public static final class Enderman extends Hostile {
        public Enderman() {
            super(EntityType.ENDERMAN, "Enderman", ItemType.ENDER_PEARL, MobType.ENDER);
        }
    }

    public static final class Ghast extends Hostile {
        public Ghast() {
            super(EntityType.GHAST, "Ghast", ItemType.GHAST_TEAR, MobType.AIRBORNE);
        }
    }

    public static final class MagmaCube extends Hostile {
        public MagmaCube() {
            super(EntityType.MAGMA_CUBE, "Magma Cube", ItemType.MAGMA_CREAM, MobType.MAGMATIC);
        }
    }

    public static final class Skeleton extends Hostile {
        public Skeleton() {
            super(EntityType.SKELETON, "Skeleton", ItemType.BONE, MobType.SKELETAL);
        }
    }

    public static final class Slime extends Hostile {
        public Slime() {
            super(EntityType.SLIME, "Slime", ItemType.SLIME_BALL, MobType.CUBIC);
        }
    }

    public static final class Spider extends Hostile {
        public Spider() {
            super(EntityType.SPIDER, "Spider", ItemType.STRING, MobType.ARTHROPOD);
        }
    }
}
