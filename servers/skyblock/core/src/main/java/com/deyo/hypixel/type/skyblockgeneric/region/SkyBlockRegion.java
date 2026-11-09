package com.deyo.hypixel.type.skyblockgeneric.region;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.data.regions.RegionCatalog;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
public class SkyBlockRegion {
    private static final Map<String, SkyBlockRegion> REGION_CACHE = new ConcurrentHashMap<>();
    private static volatile List<SkyBlockRegion> regionList = List.of();

    private final String name;
    private Pos firstLocation;
    private Pos secondLocation;
    private RegionType type;
    private ServerType serverType;

    public SkyBlockRegion(String name, Pos firstLocation, Pos secondLocation, RegionType type, ServerType serverType) {
        this.name = name.toLowerCase();
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
        this.type = type;
        this.serverType = serverType;
        invalidateBounds();
    }

    public void setFirstLocation(Pos firstLocation) {
        this.firstLocation = firstLocation;
        invalidateBounds();
    }

    public void setSecondLocation(Pos secondLocation) {
        this.secondLocation = secondLocation;
        invalidateBounds();
    }

    public void save() {
        RegionCatalog.save(this);
        REGION_CACHE.put(name, this);
        regionList = List.copyOf(REGION_CACHE.values());
    }

    public Pos getRandomPosition() {
        ensureBounds();

        int x = maxX >= minX ? ThreadLocalRandom.current().nextInt(minX, maxX + 1) : minX;
        int y = maxY >= minY ? ThreadLocalRandom.current().nextInt(minY, maxY + 1) : minY;
        int z = maxZ >= minZ ? ThreadLocalRandom.current().nextInt(minZ, maxZ + 1) : minZ;

        return new Pos(x, y, z);
    }

    /**
     * Gets a random position for an entity to spawn in the region
     *
     * @param instance The instance to check if the position is valid in
     * @return A random position in the region, or null if there is no valid position
     * @see SkyBlockMob#spawn()
     * @see SkyBlockMob#setInstance(Instance, Pos)
     */
    public @Nullable Pos getRandomPositionForEntity(Instance instance) {
        // Checks if there is a 2 block gap up the Y level and if the block below is solid

        int tries = 0;
        while (true) {
            tries++;
            Pos randomPosition = getRandomPosition();
            Pos blockAbove = randomPosition.add(0, 1, 0);
            Pos blockBelow = randomPosition.sub(0, 1, 0);

            if (tries > 5) {
                return null;
            }

            if (instance.isChunkLoaded(randomPosition)
                && instance.getBlock(randomPosition).air()
                && instance.getBlock(blockAbove).air()
                && !instance.getBlock(blockBelow).air())
                return randomPosition;
        }
    }

    public void delete() {
        REGION_CACHE.remove(name);
        RegionCatalog.delete(name);
        regionList = List.copyOf(REGION_CACHE.values());
    }

    public static List<SkyBlockRegion> getRegions() {
        return regionList;
    }

    public static SkyBlockRegion getFromID(String id) {
        if (REGION_CACHE.containsKey(id.toLowerCase()))
            return REGION_CACHE.get(id.toLowerCase());
        return null;
    }

    public static SkyBlockRegion getRegionOfEntity(Entity entity) {
        return getRegionOfPosition(entity.getPosition());
    }

    public static List<SkyBlockMob> getMobsInRegion(RegionType region) {
        List<SkyBlockMob> mobs = new ArrayList<>();
        for (SkyBlockMob mob : SkyBlockMob.getMobs()) {
            SkyBlockRegion regionOfEntity = getRegionOfEntity(mob);

            if (regionOfEntity != null && regionOfEntity.getType() == region)
                mobs.add(mob);
        }
        return mobs;
    }

    public static SkyBlockRegion getRegionOfPosition(Point point) {
        return getRegionOfPosition(point.asPos());
    }

    public static SkyBlockRegion getRandomRegionOfType(RegionType type) {
        SkyBlockRegion selected = null;
        int matching = 0;
        for (SkyBlockRegion region : regionList) {
            if (region.getType() != type) continue;
            matching++;
            if (Math.random() * matching < 1) {
                selected = region;
            }
        }
        return selected;
    }

    private int minX, maxX, minY, maxY, minZ, maxZ;
    private long cachedVolume = -1;
    private boolean boundsCached;

    private void invalidateBounds() {
        boundsCached = false;
        cachedVolume = -1;
    }

    private void ensureBounds() {
        if (boundsCached) {
            return;
        }
        boundsCached = true;

        if (firstLocation == null || secondLocation == null) {
            cachedVolume = 0;
            return;
        }

        minX = Math.min(firstLocation.blockX(), secondLocation.blockX());
        maxX = Math.max(firstLocation.blockX(), secondLocation.blockX());
        minY = Math.min(firstLocation.blockY(), secondLocation.blockY());
        maxY = Math.max(firstLocation.blockY(), secondLocation.blockY());
        minZ = Math.min(firstLocation.blockZ(), secondLocation.blockZ());
        maxZ = Math.max(firstLocation.blockZ(), secondLocation.blockZ());

        long x = (long) maxX - minX + 1;
        long y = (long) maxY - minY + 1;
        long z = (long) maxZ - minZ + 1;
        cachedVolume = x * y * z;
    }

    public long getVolume() {
        ensureBounds();
        return cachedVolume;
    }

    public static SkyBlockRegion getRegionOfPosition(Pos position) {
        return getRegionOfPosition(position.x(), position.y(), position.z());
    }

    public static SkyBlockRegion getRegionOfPosition(double x, double y, double z) {
        SkyBlockRegion smallest = null;
        long smallestVolume = Long.MAX_VALUE;

        for (SkyBlockRegion region : regionList) {
            if (!region.insideRegion(x, y, z))
                continue;

            long volume = region.getVolume();
            if (volume < smallestVolume) {
                smallestVolume = volume;
                smallest = region;
            }
        }

        return smallest;
    }

    public boolean insideRegion(Entity entity) {
        return insideRegion(entity.getPosition());
    }

    public boolean insideRegion(Pos location) {
        return insideRegion(location.x(), location.y(), location.z());
    }

    public boolean insideRegion(double x, double y, double z) {
        if (firstLocation == null || secondLocation == null)
            return false;

        ensureBounds();

        return x >= minX && x <= maxX &&
            y >= minY && y <= maxY &&
            z >= minZ && z <= maxZ;
    }

    public List<Integer> getBounds() {
        ensureBounds();
        return Arrays.asList(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public static void cacheRegions() {
        REGION_CACHE.clear();
        for (SkyBlockRegion region : RegionCatalog.getAllRegions()) {
            if (region.getType() == null || region.getType() == RegionType.PRIVATE_ISLAND) {
                continue;
            }
            ServerType typeOfRegion = region.getServerType();
            if (typeOfRegion != HypixelConst.getTypeLoader().getType())
                continue;

            REGION_CACHE.put(region.getName(), region);
        }
        REGION_CACHE.put("island", new SkyBlockRegion("island",
            new Pos(0, 0, 0),
            new Pos(0, 0, 0),
            RegionType.PRIVATE_ISLAND,
            ServerType.SKYBLOCK_ISLAND));
        regionList = List.copyOf(REGION_CACHE.values());
    }

    public static SkyBlockRegion getIslandRegion() {
        return REGION_CACHE.get("island");
    }
}
