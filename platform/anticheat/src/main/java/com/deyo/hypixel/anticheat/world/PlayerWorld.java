package com.deyo.hypixel.anticheat.world;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerWorld {
    private static final int CHUNK_SIZE = 16;
    private final Map<ChunkCoordinate, Chunk> chunks;

    private final ExecutorService executorService;
    private final ReentrantLock lock;

    public PlayerWorld() {
        this.chunks = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.lock = new ReentrantLock();
    }

    public CompletableFuture<Void> updateBlock(int x, int y, int z, Block block) {
        return CompletableFuture.runAsync(() -> {
            lock.lock();
            try {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(
                        Math.floorDiv(x, CHUNK_SIZE), Math.floorDiv(z, CHUNK_SIZE));
                Chunk chunk = chunks.computeIfAbsent(chunkCoord, k -> new Chunk());
                chunk.setBlock(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE), block);
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public CompletableFuture<Block> getBlock(int x, int y, int z) {
        return CompletableFuture.supplyAsync(() -> getBlockNow(x, y, z), executorService);
    }

    /**
     * Reads a block from the current snapshot without waiting on the executor.
     * Used by movement checks that already run on the server tick thread.
     */
    public Block getBlockNow(int x, int y, int z) {
        lock.lock();
        try {
            ChunkCoordinate chunkCoord = new ChunkCoordinate(
                    Math.floorDiv(x, CHUNK_SIZE), Math.floorDiv(z, CHUNK_SIZE));
            Chunk chunk = chunks.get(chunkCoord);
            if (chunk == null) {
                return null;
            }
            return chunk.getBlock(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE));
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private static class ChunkCoordinate {
        final int x;
        final int z;

        ChunkCoordinate(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkCoordinate that = (ChunkCoordinate) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }
    }

    private static class Chunk {
        private final Map<Integer, Block> blocks = new HashMap<>();

        public void setBlock(int x, int y, int z, Block block) {
            int key = key(x, y, z);
            if (block == null) {
                blocks.remove(key);
            } else {
                blocks.put(key, block);
            }
        }

        public Block getBlock(int x, int y, int z) {
            return blocks.get(key(x, y, z));
        }

        private static int key(int x, int y, int z) {
            return (x << 12) | (z << 8) | y;
        }
    }
}
