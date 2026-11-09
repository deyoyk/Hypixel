package com.deyo.hypixel.type.generic.utility;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetGameCountsProtocol;
import com.deyo.hypixel.proxyapi.ProxyService;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class NetworkCountCache {
    private static final long CACHE_TTL_MS = 5_000;
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);

    private static final AtomicInteger PLAYER_COUNT = new AtomicInteger();
    private static final AtomicBoolean REFRESHING = new AtomicBoolean();
    private static volatile long lastUpdated;

    private NetworkCountCache() {
    }

    public static int getPlayerCount() {
        if (System.currentTimeMillis() - lastUpdated > CACHE_TTL_MS && REFRESHING.compareAndSet(false, true)) {
            Thread.startVirtualThread(() -> {
                try {
                    var message = new GetGameCountsProtocol.GetGameCountsMessage(null, null, null);
                    ORCHESTRATOR.<GetGameCountsProtocol.GetGameCountsMessage,
                                    GetGameCountsProtocol.GetGameCountsResponse>handleRequest(message)
                            .thenAccept(response -> {
                                if (response != null && response.success()) {
                                    PLAYER_COUNT.set(response.playerCount());
                                }
                            })
                            .join();
                    lastUpdated = System.currentTimeMillis();
                } catch (RuntimeException exception) {
                    lastUpdated = System.currentTimeMillis();
                } finally {
                    REFRESHING.set(false);
                }
            });
        }
        return PLAYER_COUNT.get();
    }
}
