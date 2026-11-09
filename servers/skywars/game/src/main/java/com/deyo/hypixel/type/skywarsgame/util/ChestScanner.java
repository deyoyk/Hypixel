package com.deyo.hypixel.type.skywarsgame.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.Section;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.palette.Palette;
import com.deyo.hypixel.commons.skywars.map.SkywarsMapsConfig;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChestScanner {

    private static final double ISLAND_RADIUS = 15.0;

    private static final int SCAN_HEIGHT = 100;

    public static ChestScanResult scanForChests(
            Instance instance,
            SkywarsMapsConfig.MapBounds bounds,
            List<Pos> islandSpawns,
            int voidY) {

        List<Pos> islandChests = new ArrayList<>();
        List<Pos> centerChests = new ArrayList<>();

        if (bounds == null || bounds.min() == null || bounds.max() == null) {
            Logger.warn("ChestScanner: No bounds defined, skipping scan");
            return new ChestScanResult(islandChests, centerChests);
        }

        ensureChunksLoaded(instance, bounds);

        int minX = (int) Math.min(bounds.min().x(), bounds.max().x());
        int maxX = (int) Math.max(bounds.min().x(), bounds.max().x());
        int minY = Math.max((int) Math.min(bounds.min().y(), bounds.max().y()), voidY);
        int maxY = Math.min((int) Math.max(bounds.min().y(), bounds.max().y()), voidY + SCAN_HEIGHT);
        if (maxY < minY) {
            minY = voidY;
            maxY = voidY + SCAN_HEIGHT;
        }
        int minZ = (int) Math.min(bounds.min().z(), bounds.max().z());
        int maxZ = (int) Math.max(bounds.min().z(), bounds.max().z());

        Logger.info("ChestScanner: Scanning area X[{},{}] Y[{},{}] Z[{},{}]",
                minX, maxX, minY, maxY, minZ, maxZ);

        int chestsFound = 0;

        for (int chunkX = minX >> 4; chunkX <= maxX >> 4; chunkX++) {
            for (int chunkZ = minZ >> 4; chunkZ <= maxZ >> 4; chunkZ++) {
                Chunk chunk = instance.getChunk(chunkX, chunkZ);
                if (chunk == null) continue;

                for (int sectionIndex = chunk.getMinSection(); sectionIndex <= chunk.getMaxSection(); sectionIndex++) {
                    int sectionBaseY = sectionIndex << 4;
                    if (sectionBaseY + 15 < minY || sectionBaseY > maxY) continue;

                    Section section = chunk.getSectionAt(sectionBaseY);
                    Palette palette = section.blockPalette();
                    if (!paletteContainsChest(palette)) {
                        continue;
                    }

                    int sectionMinY = Math.max(sectionBaseY, minY);
                    int sectionMaxY = Math.min(sectionBaseY + 15, maxY);
                    for (int x = Math.max(minX, chunkX << 4); x <= Math.min(maxX, (chunkX << 4) + 15); x++) {
                        for (int y = sectionMinY; y <= sectionMaxY; y++) {
                            for (int z = Math.max(minZ, chunkZ << 4); z <= Math.min(maxZ, (chunkZ << 4) + 15); z++) {
                                Block block = instance.getBlock(x, y, z);

                                if (block.compare(Block.CHEST) || block.compare(Block.TRAPPED_CHEST)) {
                                    Pos chestPos = new Pos(x, y, z);
                                    chestsFound++;

                                    if (isIslandChest(chestPos, islandSpawns)) {
                                        islandChests.add(chestPos);
                                    } else {
                                        centerChests.add(chestPos);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Logger.info("ChestScanner: Found {} chests ({} island, {} center)",
                chestsFound, islandChests.size(), centerChests.size());

        return new ChestScanResult(islandChests, centerChests);
    }

    static boolean paletteContainsChest(Palette palette) {
        return !palette.isEmpty()
                && (palette.any(Block.CHEST.stateId()) || palette.any(Block.TRAPPED_CHEST.stateId()));
    }

    private static boolean isIslandChest(Pos chestPos, List<Pos> islandSpawns) {
        for (Pos island : islandSpawns) {
            double distance = chestPos.distance(island);
            if (distance <= ISLAND_RADIUS) {
                return true;
            }
        }
        return false;
    }

    private static void ensureChunksLoaded(Instance instance, SkywarsMapsConfig.MapBounds bounds) {
        int minChunkX = (int) Math.min(bounds.min().x(), bounds.max().x()) >> 4;
        int maxChunkX = (int) Math.max(bounds.min().x(), bounds.max().x()) >> 4;
        int minChunkZ = (int) Math.min(bounds.min().z(), bounds.max().z()) >> 4;
        int maxChunkZ = (int) Math.max(bounds.min().z(), bounds.max().z()) >> 4;

        List<CompletableFuture<Chunk>> futures = new ArrayList<>();

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                Chunk chunk = instance.getChunk(cx, cz);
                if (chunk == null) {
                    futures.add(instance.loadChunk(cx, cz));
                }
            }
        }

        if (!futures.isEmpty()) {
            Logger.debug("ChestScanner: Loading {} chunks", futures.size());
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    public record ChestScanResult(List<Pos> islandChests, List<Pos> centerChests) {}
}
