package com.deyo.hypixel.service.itemtracker;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrackedItemsDatabaseConcurrencyTest {

    @Test
    void updatesForTheSameItemAreSerialized() throws Exception {
        UUID itemUuid = UUID.randomUUID();
        int workers = 8;
        AtomicInteger running = new AtomicInteger();
        AtomicInteger maxRunning = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(workers);

        List<Thread> threads = new ArrayList<>();
        for (int index = 0; index < workers; index++) {
            threads.add(Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    TrackedItemsDatabase.withItemLock(itemUuid, () -> {
                        int nowRunning = running.incrementAndGet();
                        maxRunning.accumulateAndGet(nowRunning, Math::max);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException exception) {
                            Thread.currentThread().interrupt();
                        }
                        running.decrementAndGet();
                        return null;
                    });
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }));
        }

        start.countDown();
        assertTrue(done.await(5, TimeUnit.SECONDS));
        assertEquals(1, maxRunning.get());
    }
}
