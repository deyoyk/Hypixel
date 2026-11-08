package com.deyo.hypixel.proxyapi;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.redis.RedisClient;

import java.util.concurrent.CompletableFuture;

public record ProxyService(ServiceType type) {
    public CompletableFuture<Boolean> isOnline() {
        return RedisClient.isServiceOnline(type);
    }

    public <T, R> CompletableFuture<R> handleRequest(T request) {
        return RedisClient.requestService(type, request);
    }

    public <T, R> CompletableFuture<R> handleRequest(RedisProtocol<T, R> protocol, T request) {
        return RedisClient.requestService(type, protocol, request);
    }
}
