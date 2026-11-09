package com.deyo.hypixel.type.generic.utility;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CooldownTracker {
    private final Map<UUID, Long> lastUse = new ConcurrentHashMap<>();

    public boolean tryUse(UUID playerId, long cooldownMillis) {
        long now = System.currentTimeMillis();
        AtomicBoolean used = new AtomicBoolean();
        lastUse.compute(playerId, (key, previous) -> {
            if (previous == null || now - previous >= cooldownMillis) {
                used.set(true);
                return now;
            }
            return previous;
        });
        return used.get();
    }

    public void clear(UUID playerId) {
        lastUse.remove(playerId);
    }

    public int size() {
        return lastUse.size();
    }
}
