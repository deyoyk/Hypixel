package com.deyo.hypixel.commons.redis;

import com.deyo.hypixel.commons.ServiceType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedisClientServiceStatusTest {

    @AfterEach
    void clearCache() {
        RedisClient.clearServiceStatusCache();
    }

    @Test
    void freshStatusSkipsTheProbe() {
        RedisClient.cacheServiceStatus(ServiceType.GUILD, true, 1_000L);
        AtomicInteger probes = new AtomicInteger();

        assertTrue(RedisClient.cachedServiceStatus(ServiceType.GUILD, 1_500L, () -> {
            probes.incrementAndGet();
            return CompletableFuture.completedFuture(false);
        }).join());

        assertEquals(0, probes.get());
    }

    @Test
    void staleStatusProbesAndCachesTheResult() {
        RedisClient.cacheServiceStatus(ServiceType.GUILD, true, 1_000L);
        AtomicInteger probes = new AtomicInteger();

        assertFalse(RedisClient.cachedServiceStatus(ServiceType.GUILD, 3_001L, () -> {
            probes.incrementAndGet();
            return CompletableFuture.completedFuture(false);
        }).join());
        assertEquals(1, probes.get());

        assertFalse(RedisClient.cachedServiceStatus(ServiceType.GUILD, 3_002L, () -> {
            probes.incrementAndGet();
            return CompletableFuture.completedFuture(true);
        }).join());
        assertEquals(1, probes.get());
    }

    @Test
    void concurrentLookupsShareOneProbe() {
        CompletableFuture<Boolean> pending = new CompletableFuture<>();

        CompletableFuture<Boolean> first = RedisClient.cachedServiceStatus(ServiceType.GUILD, 1_000L, () -> pending);
        CompletableFuture<Boolean> second = RedisClient.cachedServiceStatus(ServiceType.GUILD, 1_001L, () -> {
            throw new AssertionError("second probe should not run");
        });

        assertSame(first, second);
        pending.complete(true);
        assertTrue(first.join());
    }
}
