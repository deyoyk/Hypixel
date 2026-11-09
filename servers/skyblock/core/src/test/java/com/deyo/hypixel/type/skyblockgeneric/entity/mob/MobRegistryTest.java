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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class MobRegistryTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void lookupsUseClassAndBestiaryIdIndexes() {
        MobRegistry.registerExtraMob(TestMob.class);

        MobRegistry registry = MobRegistry.getFromMob(new TestMob());

        assertNotNull(registry);
        assertSame(TestMob.class, registry.getClazz());
        assertNotNull(MobRegistry.getMobById("TEST_MOB"));
    }

    @Test
    void unregisteredMobsResolveToNull() {
        assertNull(MobRegistry.getFromMob(new UnregisteredMob()));
        assertNull(MobRegistry.getMobById("UNREGISTERED_MOB"));
    }

    @Test
    void duplicateRegistrationsAreIgnored() {
        MobRegistry.registerExtraMob(TestMob.class);
        MobRegistry.registerExtraMob(TestMob.class);

        long registrations = MobRegistry.REGISTERED_MOBS.stream()
                .filter(registry -> registry.getClazz() == TestMob.class)
                .count();

        assertEquals(1, registrations);
    }

    @Test
    void countsMobsByTheirRegistry() {
        MobRegistry.registerExtraMob(TestMob.class);

        Map<MobRegistry, Integer> counts = SkyBlockMob.countMobsByRegistry(
                List.of(new TestMob(), new TestMob(), new UnregisteredMob()));

        assertEquals(2, counts.get(MobRegistry.getFromMob(new TestMob())));
    }

    static class TestMob extends BestiaryMob {
        TestMob() {
            super(EntityType.ZOMBIE);
        }

        @Override
        public String getDisplayName() {
            return "Test Mob";
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
            return ItemStatistics.builder()
                    .withBase(ItemStatistic.HEALTH, 20D)
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
            return "TEST_MOB";
        }

        @Override
        public GUIMaterial getGuiMaterial() {
            return new GUIMaterial(Material.ZOMBIE_HEAD);
        }
    }

    static final class UnregisteredMob extends TestMob {
    }
}
