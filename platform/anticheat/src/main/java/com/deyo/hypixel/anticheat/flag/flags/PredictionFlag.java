package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.engine.PlayerTickInformation;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerKnockbackEvent;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;
import com.deyo.hypixel.anticheat.math.Pos;
import com.deyo.hypixel.anticheat.math.Vel;
import com.deyo.hypixel.anticheat.prediction.PlayerContext;
import com.deyo.hypixel.anticheat.prediction.PredictionEngine;
import com.deyo.hypixel.anticheat.prediction.PredictionEngine.PredictedMovement;
import com.deyo.hypixel.anticheat.prediction.compensation.LagCompensator;
import com.deyo.hypixel.anticheat.prediction.modifier.impl.*;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Modular prediction-based movement check
 * Uses abstract modifiers for extensibility and proper lag compensation
 */
public class PredictionFlag extends Flag {

    // Shared prediction engine (registered modifiers are reused)
    private static final PredictionEngine engine = new PredictionEngine();
    private static final LagCompensator lagCompensator = new LagCompensator();

    // Track transaction IDs per player
    private static final Map<UUID, Integer> currentTransactions = new ConcurrentHashMap<>();

    public static void clearPlayer(UUID uuid) {
        currentTransactions.remove(uuid);
    }

    static {
        // Register all velocity modifiers (sorted by priority automatically)
        engine.registerVelocityModifier(new CobwebModifier());       // Very high priority
        engine.registerVelocityModifier(new LevitationModifier());
        engine.registerVelocityModifier(new BubbleColumnModifier());
        engine.registerVelocityModifier(new SlowFallingModifier());
        engine.registerVelocityModifier(new SneakModifier());
        engine.registerVelocityModifier(new SprintModifier());
        engine.registerVelocityModifier(new UsingItemModifier());
        engine.registerVelocityModifier(new SpeedEffectModifier());
        engine.registerVelocityModifier(new SlownessEffectModifier());
        engine.registerVelocityModifier(new SoulSpeedModifier());
        engine.registerVelocityModifier(new DepthStriderModifier());
        engine.registerVelocityModifier(new DolphinsGraceModifier());
        engine.registerVelocityModifier(new HoneyBlockModifier());
        engine.registerVelocityModifier(new SoulSandModifier());

        // Register all friction modifiers (sorted by priority automatically)
        engine.registerFrictionModifier(new BlueIceFrictionModifier());
        engine.registerFrictionModifier(new IceFrictionModifier());
        engine.registerFrictionModifier(new SlimeFrictionModifier());
    }

    @ListenerMethod
    public void onPlayerKnockback(PlayerKnockbackEvent event) {
        event.getPlayer().handleKnockback(event.getKnockbackVelocity());
    }

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        // Disabled until the prediction thresholds are validated against live movement.
    }

    /**
     * Build comprehensive player context from all available player data
     */
    private PlayerContext buildPlayerContext(DeyoPlayer player, PlayerTickInformation tick) {
        Pos pos = tick.getPos();

        // Get blocks from player world
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y());
        int blockZ = (int) Math.floor(pos.z());
        Instance instance = onlineInstance(player);

        Block minestomAt = instance == null ? null : instance.getBlock(blockX, blockY, blockZ);
        Block minestomBelow = instance == null ? null : instance.getBlock(blockX, blockY - 1, blockZ);
        Block minestomAbove = instance == null ? null : instance.getBlock(blockX, blockY + 1, blockZ);

        boolean inWater = matchesBlockType(minestomAt, "WATER");
        boolean inLava = matchesBlockType(minestomAt, "LAVA");
        boolean inBubbleColumn = matchesBlockType(minestomAt, "BUBBLE_COLUMN");
        boolean inCobweb = matchesBlockType(minestomAt, "COBWEB");
        boolean onIce = matchesBlockType(minestomBelow, "ICE", "PACKED_ICE");
        boolean onSlime = matchesBlockType(minestomBelow, "SLIME_BLOCK");
        boolean onHoney = matchesBlockType(minestomBelow, "HONEY_BLOCK");
        boolean onSoulSand = matchesBlockType(minestomBelow, "SOUL_SAND", "SOUL_SOIL");

        return PlayerContext.builder()
            .position(pos)
            .velocity(tick.getVel())
            .onGround(tick.isOnGround())
            .wasOnGround(tick.getPrevious() != null && tick.getPrevious().isOnGround())

            .blockAt(toContextBlock(minestomAt))
            .blockBelow(toContextBlock(minestomBelow))
            .blockAbove(toContextBlock(minestomAbove))
            .inWater(inWater)
            .inLava(inLava)
            .inBubbleColumn(inBubbleColumn)
            .inCobweb(inCobweb)
            .onSoulSand(onSoulSand)
            .onIce(onIce)
            .onSlime(onSlime)
            .onHoney(onHoney)

            .speedLevel(getEffectLevel(player, "SPEED"))
            .slownessLevel(getEffectLevel(player, "SLOWNESS"))
            .jumpBoostLevel(getEffectLevel(player, "JUMP_BOOST"))
            .levitationLevel(getEffectLevel(player, "LEVITATION"))
            .slowFallingLevel(getEffectLevel(player, "SLOW_FALLING"))
            .hasDolphinsGrace(hasEffect(player, "DOLPHINS_GRACE"))

            .depthStriderLevel(getEnchantmentLevel(player, "DEPTH_STRIDER"))
            .soulSpeedLevel(getEnchantmentLevel(player, "SOUL_SPEED"))
            .hasFrostWalker(hasEnchantment(player, "FROST_WALKER"))

            .sprinting(getPlayerState(player, "sprinting"))
            .sneaking(getPlayerState(player, "sneaking"))
            .swimming(getPlayerState(player, "swimming"))
            .gliding(getPlayerState(player, "gliding"))
            .flying(getPlayerState(player, "flying"))
            .usingItem(getPlayerState(player, "usingItem"))

            // Latency compensation
            .ping(player.getPing())
            .transactionPing(getTransactionPing(player))
            .skippedTicks(0)

            // Knockback tracking
            .expectedKnockback(getExpectedKnockback(player))
            .ticksSinceKnockback(getTicksSinceKnockback(player))

            .build();
    }

    private static Instance onlineInstance(DeyoPlayer player) {
        Player online = onlinePlayer(player);
        return online == null ? null : online.getInstance();
    }

    static com.deyo.hypixel.anticheat.world.Block blockAt(Instance instance, int x, int y, int z) {
        return instance == null ? null : toContextBlock(instance.getBlock(x, y, z));
    }

    static com.deyo.hypixel.anticheat.world.Block toContextBlock(Block block) {
        if (block == null) return null;
        String name = block.key().value();
        return new com.deyo.hypixel.anticheat.world.Block(
            name.equals("water"),
            name.equals("lava"),
            null
        );
    }

    static boolean matchesBlockType(Block block, String... types) {
        if (block == null || block.air()) return false;
        String name = block.key().value();
        for (String type : types) {
            if (name.equalsIgnoreCase(type)) return true;
        }
        return false;
    }

    private int getEffectLevel(DeyoPlayer player, String effect) {
        Player online = onlinePlayer(player);
        if (online == null) return 0;
        PotionEffect potionEffect = effectFor(effect);
        return potionEffect == null ? 0 : online.getEffectLevel(potionEffect);
    }

    private boolean hasEffect(DeyoPlayer player, String effect) {
        Player online = onlinePlayer(player);
        if (online == null) return false;
        PotionEffect potionEffect = effectFor(effect);
        return potionEffect != null && online.hasEffect(potionEffect);
    }

    static PotionEffect effectFor(String effect) {
        return switch (effect) {
            case "SPEED" -> PotionEffect.SPEED;
            case "SLOWNESS" -> PotionEffect.SLOWNESS;
            case "JUMP_BOOST" -> PotionEffect.JUMP_BOOST;
            case "LEVITATION" -> PotionEffect.LEVITATION;
            case "SLOW_FALLING" -> PotionEffect.SLOW_FALLING;
            case "DOLPHINS_GRACE" -> PotionEffect.DOLPHINS_GRACE;
            default -> null;
        };
    }

    private int getEnchantmentLevel(DeyoPlayer player, String enchantment) {
        Player online = onlinePlayer(player);
        if (online == null) return 0;
        return enchantmentLevel(online.getEquipment(EquipmentSlot.BOOTS).get(DataComponents.ENCHANTMENTS), enchantment);
    }

    static int enchantmentLevel(EnchantmentList enchantments, String enchantment) {
        if (enchantments == null) return 0;
        return switch (enchantment) {
            case "DEPTH_STRIDER" -> enchantments.level(Enchantment.DEPTH_STRIDER);
            case "SOUL_SPEED" -> enchantments.level(Enchantment.SOUL_SPEED);
            case "FROST_WALKER" -> enchantments.level(Enchantment.FROST_WALKER);
            default -> 0;
        };
    }

    private boolean hasEnchantment(DeyoPlayer player, String enchantment) {
        return getEnchantmentLevel(player, enchantment) > 0;
    }

    private boolean getPlayerState(DeyoPlayer player, String state) {
        Player online = onlinePlayer(player);
        if (online == null) return false;
        return switch (state) {
            case "sprinting" -> online.isSprinting();
            case "sneaking" -> online.isSneaking();
            case "swimming" -> online.getPose() == EntityPose.SWIMMING;
            case "gliding" -> online.getPose() == EntityPose.FALL_FLYING;
            case "flying" -> online.isFlying();
            case "usingItem" -> online.getCurrentItemUseTime() > 0;
            default -> false;
        };
    }

    private long getTransactionPing(DeyoPlayer player) {
        return Math.max(0, player.getPing());
    }

    private Vel getExpectedKnockback(DeyoPlayer player) {
        return expectedKnockback(player);
    }

    static Vel expectedKnockback(DeyoPlayer player) {
        return player.getTicksSinceKnockback() <= 3
                ? player.getExpectedKnockback()
                : new Vel(0, 0, 0);
    }

    private int getTicksSinceKnockback(DeyoPlayer player) {
        return player.getTicksSinceKnockback();
    }

    /**
     * Find closest prediction to actual movement
     */
    private PredictionResult findClosestPrediction(List<PredictedMovement> predictions,
                                                    Pos actualPos, Vel actualVel) {
        double minOffset = Double.MAX_VALUE;
        PredictedMovement closest = null;

        for (PredictedMovement prediction : predictions) {
            double posOffset = calculateDistance(prediction.getPosition(), actualPos);
            double velOffset = calculateDistance(
                prediction.getVelocity().asPosition(),
                actualVel.asPosition()
            );

            // Weight position more heavily than velocity
            double totalOffset = posOffset * 0.8 + velOffset * 0.2;

            // Add prediction uncertainty
            totalOffset -= prediction.getUncertainty();

            if (totalOffset < minOffset) {
                minOffset = totalOffset;
                closest = prediction;
            }
        }

        return new PredictionResult(closest, Math.max(0, minOffset));
    }

    private double calculateDistance(Pos pos1, Pos pos2) {
        double dx = pos1.x() - pos2.x();
        double dy = pos1.y() - pos2.y();
        double dz = pos1.z() - pos2.z();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Calculate certainty based on offset from predictions
     */
    private double calculateCertainty(double offset) {
        // With proper modifiers and lag compensation, we can be very precise
        if (offset < 0.0001) return 0.0; // Perfect match
        if (offset < 0.001) return 0.1;   // Rounding error
        if (offset < 0.01) return 0.4;    // Slightly suspicious
        if (offset < 0.05) return 0.7;    // Moderately suspicious
        if (offset < 0.1) return 0.85;    // Very suspicious
        return 0.95;                       // Extremely suspicious / blatant
    }

    private static class PredictionResult {
        final PredictedMovement prediction;
        final double offset;

        PredictionResult(PredictedMovement prediction, double offset) {
            this.prediction = prediction;
            this.offset = offset;
        }
    }
}
