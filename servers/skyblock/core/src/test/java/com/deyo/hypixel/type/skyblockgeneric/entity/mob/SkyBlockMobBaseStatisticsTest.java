package com.deyo.hypixel.type.skyblockgeneric.entity.mob;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIMaterial;
import com.deyo.hypixel.type.skyblockgeneric.loottable.OtherLoot;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkyBlockMobBaseStatisticsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void baseStatisticsAreReadOnlyOnceDuringConstruction() {
        CountingMob mob = new CountingMob();

        assertEquals(1, mob.calls);
    }

    static final class CountingMob extends BestiaryMob {
        int calls;

        CountingMob() {
            super(EntityType.ZOMBIE);
        }

        @Override
        public String getDisplayName() {
            return "Counting Mob";
        }

        @Override
        public Integer getLevel() {
            return 1;
        }

        @Override
        public List<GoalSelector> getGoalSelectors() {
            return List.of();
        }

        @Override
        public List<TargetSelector> getTargetSelectors() {
            return List.of();
        }

        @Override
        public ItemStatistics getBaseStatistics() {
            calls++;
            return ItemStatistics.builder()
                    .withBase(ItemStatistic.HEALTH, 20D)
                    .withBase(ItemStatistic.SPEED, 100D)
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
            return 500;
        }

        @Override
        public OtherLoot getOtherLoot() {
            return new OtherLoot(0, 0, 0);
        }

        @Override
        public int getMaxBestiaryTier() {
            return 1;
        }

        @Override
        public int getBestiaryBracket() {
            return 1;
        }

        @Override
        public String getMobID() {
            return "COUNTING_MOB";
        }

        @Override
        public GUIMaterial getGuiMaterial() {
            return new GUIMaterial(Material.ZOMBIE_HEAD);
        }
    }
}
