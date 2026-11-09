package com.deyo.hypixel.type.skyblockgeneric.entity.mob;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.impl.RegionPopulator;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class MobRegistry {
    public static final List<MobRegistry> REGISTERED_MOBS = new CopyOnWriteArrayList<>();
    private static final Map<Class<? extends SkyBlockMob>, MobRegistry> MOBS_BY_CLASS = new ConcurrentHashMap<>();
    private static final Map<String, BestiaryMob> MOBS_BY_ID = new ConcurrentHashMap<>();
    private static volatile List<MobRegistry> regionPopulators = List.of();

    private final EntityType entityType;
    private final Class<? extends SkyBlockMob> clazz;
    private final SkyBlockMob mobCache;

    public MobRegistry(Class<? extends SkyBlockMob> clazz) {
        this.clazz = clazz;
        this.mobCache = asMob();
        this.entityType = mobCache.getEntityType();
    }

    public SkyBlockMob asMob() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            Logger.error(e, "Failed to register mob in MobRegistry");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void registerExtraMob(Class<? extends SkyBlockMob> clazz) {
        if (MOBS_BY_CLASS.containsKey(clazz)) {
            return;
        }

        MobRegistry registry = new MobRegistry(clazz);
        if (MOBS_BY_CLASS.putIfAbsent(clazz, registry) != null) {
            return;
        }

        REGISTERED_MOBS.add(registry);

        if (registry.getMobCache() instanceof BestiaryMob mob && mob.getMobID() != null) {
            MOBS_BY_ID.putIfAbsent(mob.getMobID(), mob);
        }

        if (registry.getMobCache() instanceof RegionPopulator) {
            regionPopulators = REGISTERED_MOBS.stream()
                    .filter(candidate -> candidate.getMobCache() instanceof RegionPopulator)
                    .toList();
        }
    }

    public static List<MobRegistry> getMobsToRegionPopulate() {
        return regionPopulators;
    }

    public static MobRegistry getFromMob(SkyBlockMob mob) {
        return MOBS_BY_CLASS.get(mob.getClass());
    }

    public static BestiaryMob getMobById(String id) {
        return MOBS_BY_ID.get(id);
    }
}
