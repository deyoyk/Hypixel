package com.deyo.hypixel.type.skyblockgeneric.enchantment.debuff;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.utility.DamageIndicator;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VenomousDebuff {
    public static final double[] SPEED_REDUCTION_PERCENTAGES = new double[]{0.05, 0.10, 0.15, 0.20, 0.25, 0.30};
    private static final int MAX_STACKS = 40;
    private static final int DURATION_SECONDS = 5;
    private static final Map<UUID, VenomousEffect> activeEffects = new ConcurrentHashMap<>();

    public static void applyVenomous(SkyBlockPlayer player, SkyBlockMob mob, double damageDealt, int level) {
        if (level < 1 || level > 6) return;

        UUID mobId = mob.getUuid();

        VenomousEffect effect = activeEffects.computeIfAbsent(mobId, k -> new VenomousEffect(player, mob));

        int oldHighestLevel = effect.getHighestLevel();

        effect.addStack(damageDealt, level);

        int newHighestLevel = effect.getHighestLevel();
        if (oldHighestLevel != newHighestLevel) {
            applySpeedReduction(mob, newHighestLevel);
        }
    }

    private static void applySpeedReduction(SkyBlockMob mob, int level) {
        double originalSpeed = mobOriginalSpeeds.computeIfAbsent(mob.getUuid(),
                ignored -> mob.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue());
        boolean catacombs = isInCatacombs(mob);

        mob.getAttribute(Attribute.MOVEMENT_SPEED)
                .setBaseValue(reducedSpeed(originalSpeed, level, catacombs));
    }

    static double reducedSpeed(double originalSpeed, int level, boolean catacombs) {
        double speedReduction = SPEED_REDUCTION_PERCENTAGES[Math.clamp(level, 1, 6) - 1];
        if (catacombs) {
            speedReduction *= 0.2;
        }
        return originalSpeed * (1.0 - speedReduction);
    }

    private static boolean isInCatacombs(SkyBlockMob mob) {
        SkyBlockRegion region = SkyBlockRegion.getRegionOfEntity(mob);
        return region != null && region.getType() == RegionType.CATACOMBS;
    }

    private static final Map<UUID, Double> mobOriginalSpeeds = new ConcurrentHashMap<>();

    public static void resetMobSpeed(SkyBlockMob mob) {
        Double originalSpeed = mobOriginalSpeeds.remove(mob.getUuid());
        if (originalSpeed != null) {
            mob.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(originalSpeed);
        }
    }

    private static class VenomousEffect {
        private final SkyBlockPlayer player;
        private final SkyBlockMob mob;
        private final List<VenomStack> stacks = new ArrayList<>();

        public VenomousEffect(SkyBlockPlayer player, SkyBlockMob mob) {
            this.player = player;
            this.mob = mob;
        }

        public void addStack(double damageDealt, int level) {
            if (stacks.size() >= MAX_STACKS) return;

            boolean wasEmpty = stacks.isEmpty();
            stacks.add(new VenomStack(damageDealt, level, System.currentTimeMillis()));

            if (wasEmpty) {
                startDamageTicks();
            }
        }

        public int getHighestLevel() {
            return stacks.stream().mapToInt(s -> s.level).max().orElse(1);
        }

        private void startDamageTicks() {
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                if (mob.isRemoved() || mob.isDead()) {
                    cleanup();
                    return;
                }

                double totalDamage = 0;
                Iterator<VenomStack> iterator = stacks.iterator();
                while (iterator.hasNext()) {
                    VenomStack stack = iterator.next();
                    long age = (System.currentTimeMillis() - stack.timestamp) / 1000;

                    if (age >= DURATION_SECONDS) {
                        iterator.remove();
                    } else {
                        double damagePercent = stack.damageDealt * 0.003 * stack.level;
                        totalDamage += damagePercent;
                    }
                }

                if (totalDamage > 0) {
                    mob.damage(new Damage(DamageType.PLAYER_ATTACK, player, player, player.getPosition(), (float) totalDamage));

                    new DamageIndicator()
                            .damage((float) totalDamage)
                            .pos(mob.getPosition())
                            .critical(false)
                            .display(mob.getInstance());
                }

                if (!stacks.isEmpty()) {
                    startDamageTicks();
                } else {
                    cleanup();
                }
            }, TaskSchedule.seconds(1), TaskSchedule.stop());
        }

        private void cleanup() {
            activeEffects.remove(mob.getUuid(), this);
            resetMobSpeed(mob);
        }
    }

    private static class VenomStack {
        public final double damageDealt;
        public final int level;
        public final long timestamp;

        public VenomStack(double damageDealt, int level, long timestamp) {
            this.damageDealt = damageDealt;
            this.level = level;
            this.timestamp = timestamp;
        }
    }
}
