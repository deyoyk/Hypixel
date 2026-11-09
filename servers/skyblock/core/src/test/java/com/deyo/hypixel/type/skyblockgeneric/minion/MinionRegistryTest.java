package com.deyo.hypixel.type.skyblockgeneric.minion;

import org.junit.jupiter.api.Test;

import com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs.MobMinionHostiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinionRegistryTest {

    @Test
    void everyRegisteredMinionHasTiersExpectationsAndAnAction() {
        for (MinionRegistry registry : MinionRegistry.values()) {
            SkyBlockMinion minion = registry.asSkyBlockMinion();

            assertFalse(minion.getTiers().isEmpty(), registry + " has no tiers");
            assertFalse(minion.getExpectations().isEmpty(), registry + " has no expectations");
            assertNotNull(minion.getAction(), registry + " has no action");
        }
    }

    @Test
    void everyMinionHostileHasALootTable() {
        assertNotNull(new MobMinionHostiles.Blaze().getLootTable());
        assertNotNull(new MobMinionHostiles.CaveSpider().getLootTable());
        assertNotNull(new MobMinionHostiles.Creeper().getLootTable());
        assertNotNull(new MobMinionHostiles.Enderman().getLootTable());
        assertNotNull(new MobMinionHostiles.Ghast().getLootTable());
        assertNotNull(new MobMinionHostiles.MagmaCube().getLootTable());
        assertNotNull(new MobMinionHostiles.Skeleton().getLootTable());
        assertNotNull(new MobMinionHostiles.Slime().getLootTable());
        assertNotNull(new MobMinionHostiles.Spider().getLootTable());
        assertNotNull(new com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs.MobMinionCow().getLootTable());
        assertNotNull(new com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs.MobMinionPig().getLootTable());
        assertNotNull(new com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs.MobMinionChicken().getLootTable());
        assertNotNull(new com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.minionMobs.MobMinionRabbit().getLootTable());
    }
}
