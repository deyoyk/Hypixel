package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.PotionEffect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.math.Vel;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        assertTrue(PredictionFlag.matchesBlockType(Block.BUBBLE_COLUMN, "BUBBLE_COLUMN"));
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

    @Test
    void mapsPredictionEffectsToPotionEffects() {
        assertEquals(PotionEffect.SPEED, PredictionFlag.effectFor("SPEED"));
        assertEquals(PotionEffect.SLOWNESS, PredictionFlag.effectFor("SLOWNESS"));
        assertEquals(PotionEffect.JUMP_BOOST, PredictionFlag.effectFor("JUMP_BOOST"));
        assertEquals(PotionEffect.LEVITATION, PredictionFlag.effectFor("LEVITATION"));
        assertEquals(PotionEffect.SLOW_FALLING, PredictionFlag.effectFor("SLOW_FALLING"));
        assertEquals(PotionEffect.DOLPHINS_GRACE, PredictionFlag.effectFor("DOLPHINS_GRACE"));
        assertNull(PredictionFlag.effectFor("UNKNOWN"));
    }

    @Test
    void knockbackIsReportedOnlyWhileFresh() {
        DeyoPlayer player = new DeyoPlayer(UUID.randomUUID());
        try {
            Vel knockback = new Vel(0.4, 0.36, 0.1);
            player.handleKnockback(knockback);

            assertSame(knockback, PredictionFlag.expectedKnockback(player));

            for (int tick = 0; tick < 4; tick++) {
                player.moveTickOn();
            }

            assertEquals(4, player.getTicksSinceKnockback());
            assertEquals(0.0, PredictionFlag.expectedKnockback(player).length());
        } finally {
            player.getWorld().shutdown();
            DeyoPlayer.players.remove(player.getUuid());
        }
    }
}
