package com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Comparator;

public class ExplodeNearTargetGoal extends GoalSelector {
    private static final double CHASE_RANGE = 8.0;
    private static final double EXPLOSION_RANGE = 2.5;
    private static final long FUSE_MS = 1500;

    private final RegionType regionType;
    private Entity target;
    private long fuseStartedAt;
    private boolean exploded;

    public ExplodeNearTargetGoal(EntityCreature entityCreature, RegionType regionType) {
        super(entityCreature);
        this.regionType = regionType;
    }

    @Override
    public boolean shouldStart() {
        if (exploded || entityCreature.isDead() || entityCreature.getInstance() == null) {
            return false;
        }
        SkyBlockRegion region = SkyBlockRegion.getRegionOfEntity(entityCreature);
        if (region == null || region.getType() != regionType) {
            return false;
        }
        target = findNearestTarget();
        return target != null;
    }

    @Override
    public void start() {
        fuseStartedAt = 0;
        pathToTarget();
    }

    @Override
    public void tick(long time) {
        if (target == null || target.isRemoved() || target.getInstance() != entityCreature.getInstance()) {
            target = findNearestTarget();
            fuseStartedAt = 0;
            if (target == null) {
                return;
            }
        }

        double distanceSquared = entityCreature.getDistanceSquared(target);
        if (distanceSquared > CHASE_RANGE * CHASE_RANGE) {
            target = null;
            fuseStartedAt = 0;
            return;
        }

        if (isWithinExplosionRange(distanceSquared)) {
            long now = System.currentTimeMillis();
            if (fuseStartedAt == 0) {
                fuseStartedAt = now;
                ((SkyBlockMob) entityCreature).getVanillaNavigator().stop();
            } else if (fuseComplete(fuseStartedAt, now)) {
                explode();
            }
        } else {
            fuseStartedAt = 0;
            pathToTarget();
        }
    }

    @Override
    public boolean shouldEnd() {
        return exploded || target == null || target.isRemoved();
    }

    @Override
    public void end() {
        ((SkyBlockMob) entityCreature).getVanillaNavigator().stop();
        target = null;
        fuseStartedAt = 0;
    }

    private Entity findNearestTarget() {
        return entityCreature.getInstance().getNearbyEntities(entityCreature.getPosition(), CHASE_RANGE).stream()
                .filter(entity -> entity instanceof SkyBlockPlayer)
                .filter(entity -> !entity.isRemoved())
                .min(Comparator.comparingDouble(entity -> entity.getDistanceSquared(entityCreature)))
                .orElse(null);
    }

    private void pathToTarget() {
        if (target == null) {
            return;
        }
        ((SkyBlockMob) entityCreature).getVanillaNavigator().pathTo(target.getPosition());
    }

    static boolean fuseComplete(long startedAt, long now) {
        return now - startedAt >= FUSE_MS;
    }

    static boolean isWithinExplosionRange(double distanceSquared) {
        return distanceSquared <= EXPLOSION_RANGE * EXPLOSION_RANGE;
    }

    private void explode() {
        exploded = true;
        SkyBlockMob mob = (SkyBlockMob) entityCreature;
        Pos position = mob.getPosition();
        float damage = mob.getBaseStatistics().getOverall(ItemStatistic.DAMAGE).floatValue();

        for (Entity entity : mob.getInstance().getNearbyEntities(position, EXPLOSION_RANGE)) {
            if (entity instanceof SkyBlockPlayer player) {
                player.damage(new Damage(DamageType.PLAYER_EXPLOSION, mob, mob, position, damage));
            }
        }

        mob.getInstance().playSound(
                Sound.sound(Key.key("entity.generic.explode"), Sound.Source.HOSTILE, 1f, 1f),
                position
        );
        mob.remove();
    }
}
