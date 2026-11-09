package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PredictionFlagTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void matchesConfiguredBlockMaterials() {
        assertTrue(PredictionFlag.matchesBlockType(Block.WATER, "WATER"));
        assertTrue(PredictionFlag.matchesBlockType(Block.PACKED_ICE, "ICE", "PACKED_ICE"));
        assertFalse(PredictionFlag.matchesBlockType(Block.STONE, "WATER"));
        assertFalse(PredictionFlag.matchesBlockType(Block.AIR, "WATER"));
    }

    @Test
    void readsBootEnchantmentLevels() {
        EnchantmentList enchantments = new EnchantmentList(Enchantment.DEPTH_STRIDER, 3);

        assertEquals(3, PredictionFlag.enchantmentLevel(enchantments, "DEPTH_STRIDER"));
        assertEquals(0, PredictionFlag.enchantmentLevel(enchantments, "SOUL_SPEED"));
        assertEquals(0, PredictionFlag.enchantmentLevel(null, "DEPTH_STRIDER"));
    }
}
