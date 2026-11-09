package com.deyo.hypixel.commons.friend;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FriendDataTest {

    @Test
    void addingTheSameFriendTwiceKeepsOneEntry() {
        FriendData data = FriendData.createEmpty(UUID.randomUUID());
        UUID friendId = UUID.randomUUID();

        data.addFriend(Friend.create(friendId));
        data.addFriend(Friend.create(friendId));

        assertEquals(1, data.getFriendCount());
    }

    @Test
    void concurrentAddsAndIterationDoNotCorruptTheList() throws Exception {
        FriendData data = new FriendData(UUID.randomUUID(), new ArrayList<>(), FriendSettings.createDefault());
        int threads = 8;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int index = 0; index < threads; index++) {
            Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    for (int iteration = 0; iteration < 200; iteration++) {
                        data.addFriend(Friend.create(UUID.randomUUID()));
                        data.getFriends().forEach(friend -> friend.getUuid().toString());
                    }
                } catch (Throwable throwable) {
                    failure.compareAndSet(null, throwable);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertEquals(true, done.await(20, TimeUnit.SECONDS));
        assertNull(failure.get());
        assertEquals(threads * 200, data.getFriendCount());
    }
}
