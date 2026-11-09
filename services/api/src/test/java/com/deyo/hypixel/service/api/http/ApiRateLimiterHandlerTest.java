package com.deyo.hypixel.service.api.http;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiRateLimiterHandlerTest {

    @Test
    void concurrentIncrementsNeverExceedTheDailyLimit() throws Exception {
        int limit = 100;
        int threads = 8;
        int attemptsPerThread = 100;
        ApiRateLimiterHandler.RequestTracker tracker = new ApiRateLimiterHandler.RequestTracker();
        AtomicInteger allowed = new AtomicInteger();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int index = 0; index < threads; index++) {
            Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    for (int attempt = 0; attempt < attemptsPerThread; attempt++) {
                        if (tracker.incrementAndCheck(limit)) {
                            allowed.incrementAndGet();
                        }
                    }
                } catch (Throwable throwable) {
                    failure.compareAndSet(null, throwable);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(10, TimeUnit.SECONDS));
        assertNull(failure.get());
        assertEquals(limit, allowed.get());
        assertEquals(limit, tracker.snapshot().dailyCount());
    }

    @Test
    void countUpdateRaisesButNeverLowersTheStoredCount() {
        Document update = ApiRateLimiterHandler.countUpdate("key", "2026-11-08", 42);

        Document max = (Document) update.get("$max");
        assertEquals(42, max.getInteger("count"));

        Document set = (Document) update.get("$set");
        assertEquals("key", set.getString("apiKey"));
        assertEquals("2026-11-08", set.getString("date"));
    }
}
