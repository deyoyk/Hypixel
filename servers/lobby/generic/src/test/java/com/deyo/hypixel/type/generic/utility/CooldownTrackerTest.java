package com.deyo.hypixel.type.generic.utility;

import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CooldownTrackerTest {

    @Test
    void unmarkedPlayersAreReady() {
        assertTrue(new CooldownTracker().tryUse(UUID.randomUUID(), 1000L));
    }

    @Test
    void tryUseEnforcesCooldown() {
        CooldownTracker cooldowns = new CooldownTracker();
        UUID playerId = UUID.randomUUID();

        assertTrue(cooldowns.tryUse(playerId, 60_000L));
        assertFalse(cooldowns.tryUse(playerId, 60_000L));
        assertTrue(cooldowns.tryUse(playerId, 0L));
    }

    @Test
    void clearForgetsPlayerState() {
        CooldownTracker cooldowns = new CooldownTracker();
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        cooldowns.tryUse(first, 60_000L);
        cooldowns.tryUse(second, 60_000L);

        cooldowns.clear(first);

        assertEquals(1, cooldowns.size());
        assertTrue(cooldowns.tryUse(first, 60_000L));
        assertFalse(cooldowns.tryUse(second, 60_000L));
    }

    @Test
    void tryUseIsAtomic() throws InterruptedException {
        CooldownTracker cooldowns = new CooldownTracker();
        UUID playerId = UUID.randomUUID();
        int attempts = 32;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(attempts);
        AtomicInteger successes = new AtomicInteger();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < attempts; i++) {
                executor.submit(() -> {
                    try {
                        start.await();
                        if (cooldowns.tryUse(playerId, 60_000L)) {
                            successes.incrementAndGet();
                        }
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    } finally {
                        done.countDown();
                    }
                });
            }
            start.countDown();
            assertTrue(done.await(5, TimeUnit.SECONDS));
        }

        assertEquals(1, successes.get());
        assertEquals(1, cooldowns.size());
    }
}
