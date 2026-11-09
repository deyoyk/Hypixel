package com.deyo.hypixel.type.skyblockgeneric.entity.mob.ai;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.generic.utility.BlockProps;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomRegionStrollGoal extends GoalSelector {

    private static final long DELAY = 2500;

    private final int radius;
    private final RegionType type;
    private final boolean avoidWater;
    private List<Vec> closePositions;

    private long lastStroll;

    public RandomRegionStrollGoal(@NotNull EntityCreature entityCreature, int radius, RegionType type) {
        this(entityCreature, radius, type, false);
    }

    public RandomRegionStrollGoal(@NotNull EntityCreature entityCreature, int radius, RegionType type, boolean avoidWater) {
        super(entityCreature);
        this.radius = radius;
        this.type = type;
        this.avoidWater = avoidWater;
    }

    @Override
    public boolean shouldStart() {
        return System.currentTimeMillis() - lastStroll >= DELAY;
    }

    @Override
    public void start() {
        if (closePositions == null) {
            closePositions = getNearbyBlocks(radius);
        }

        int remainingAttempt = closePositions.size();

        if (remainingAttempt == 0) {
            end();
            return;
        }

        while (remainingAttempt-- > 0) {
            final int index = ThreadLocalRandom.current().nextInt(closePositions.size());
            final Vec position = closePositions.get(index);

            final var target = entityCreature.getPosition().add(position);
            final boolean result = ((SkyBlockMob) entityCreature).getVanillaNavigator().pathTo(target);
            if (result) {
                break;
            }
        }
    }

    @Override
    public void tick(long time) {}

    @Override
    public boolean shouldEnd() {
        return true;
    }

    @Override
    public void end() {
        this.lastStroll = System.currentTimeMillis();
    }

    private @NotNull List<Vec> getNearbyBlocks(int radius) {
        List<Vec> blocks = new ArrayList<>();

        Instance instance = entityCreature.getInstance();
        int baseX = entityCreature.getPosition().blockX();
        int baseY = entityCreature.getPosition().blockY();
        int baseZ = entityCreature.getPosition().blockZ();

        if (instance != null) {
            int minChunkX = (baseX - radius) >> 4;
            int maxChunkX = (baseX + radius) >> 4;
            int minChunkZ = (baseZ - radius) >> 4;
            int maxChunkZ = (baseZ + radius) >> 4;
            for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
                for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                    if (!instance.isChunkLoaded(chunkX, chunkZ)) {
                        instance.loadChunk(chunkX, chunkZ).join();
                    }
                }
            }
        }

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int entityX = baseX + x;
                int entityZ = baseZ + z;

                if (instance != null) {
                    Block block = instance.getBlock(entityX, baseY, entityZ);

                    if (avoidWater && BlockProps.isWater(block)) continue;

                    if (!block.air()) continue;
                }

                SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(entityX, baseY, entityZ);

                if (region == null)
                    continue;

                if (region.getType() == type)
                    blocks.add(new Vec(x, baseY, z));
            }
        }

        return blocks;
    }
}