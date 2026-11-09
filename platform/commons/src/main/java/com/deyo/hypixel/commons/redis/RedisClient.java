package com.deyo.hypixel.commons.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.PingProtocol;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisClient {
    private static final Map<String, RedisProtocol<?, ?>> protocolsByRequest = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService TIMEOUTS = Executors.newSingleThreadScheduledExecutor(task -> {
        Thread thread = new Thread(task, "redis-client-timeouts");
        thread.setDaemon(true);
        return thread;
    });
    private static final long SERVICE_STATUS_TTL_MS = 2000;
    private static final Map<ServiceType, CachedServiceStatus> SERVICE_STATUS = new ConcurrentHashMap<>();
    private static final Map<ServiceType, CompletableFuture<Boolean>> SERVICE_PROBES = new ConcurrentHashMap<>();
    private static volatile RedisEndpoint localEndpoint;

    private record CachedServiceStatus(boolean online, long checkedAt) {
    }

    public static void identify(RedisEndpoint endpoint) {
        localEndpoint = endpoint;
    }

    public static RedisEndpoint localEndpoint() {
        if (localEndpoint == null) {
            throw new IllegalStateException("RedisClient local endpoint has not been identified");
        }
        return localEndpoint;
    }

    public static void registerResponseProtocol(RedisProtocol<?, ?> protocol) {
        protocolsByRequest.put(requestTypeName(protocol), protocol);
        RedisMessageBus.registerResponseChannel(RedisChannels.protocol(protocol));
    }

    public static void registerResponseChannel(String channel) {
        RedisMessageBus.registerResponseChannel(channel);
    }

    public static void registerResponseProtocols(Iterable<? extends RedisProtocol<?, ?>> protocols) {
        protocols.forEach(RedisClient::registerResponseProtocol);
    }

    public static <T, R> CompletableFuture<R> requestProxy(RedisProtocol<T, R> protocol, T message) {
        return request(RedisEndpoint.proxy().id(), RedisChannels.protocol(protocol), RedisChannels.protocol(protocol), protocol, message);
    }

    public static <T, R> CompletableFuture<R> requestServer(UUID server, RedisProtocol<T, R> protocol, T message) {
        return request(server.toString(), RedisChannels.protocol(protocol), RedisChannels.protocol(protocol), protocol, message);
    }

    public static <T, R> CompletableFuture<R> requestService(ServiceType service, RedisProtocol<T, R> protocol, T message) {
        return request(service.name(), RedisChannels.protocol(protocol), RedisChannels.protocol(protocol), protocol, message);
    }

    public static <T, R> CompletableFuture<R> requestService(ServiceType service, T message) {
        RedisProtocol<T, R> protocol = protocolFor(message);
        return requestService(service, protocol, message);
    }

    public static <T, R> CompletableFuture<R> requestServiceFromServer(ServiceType service, RedisProtocol<T, R> protocol, T message) {
        return requestService(service, protocol, message);
    }

    public static <T> void publishService(ServiceType service, RedisProtocol<T, ?> protocol, T message) {
        RedisMessageBus.publish(localEndpoint(), service.name(), RedisChannels.protocol(protocol), protocol, message);
    }

    public static <T, R> CompletableFuture<R> requestServerFromService(UUID server,
                                                                        RedisProtocol<T, R> protocol,
                                                                        T message) {
        return RedisMessageBus.request(
                localEndpoint(),
                server.toString(),
                RedisChannels.serviceRequest(protocol),
                RedisChannels.SERVICE_RESPONSE,
                protocol,
                message
        );
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> requestAllServersFromService(RedisProtocol<T, R> protocol,
                                                                                       T message,
                                                                                       int timeoutMs) {
        return RedisMessageBus.requestBroadcast(
                localEndpoint(),
                RedisChannels.ALL_SERVERS,
                RedisChannels.serviceBroadcast(protocol),
                RedisChannels.SERVICE_BROADCAST_RESPONSE,
                protocol,
                message,
                timeoutMs
        );
    }

    public static <T, R> CompletableFuture<Map<UUID, R>> requestServersFromService(Iterable<UUID> servers,
                                                                                    RedisProtocol<T, R> protocol,
                                                                                    T message) {
        Map<UUID, CompletableFuture<R>> futures = new HashMap<>();
        for (UUID server : servers) {
            futures.put(server, requestServerFromService(server, protocol, message));
        }

        return CompletableFuture.allOf(futures.values().toArray(CompletableFuture[]::new))
                .thenApply(ignored -> {
                    Map<UUID, R> responses = new HashMap<>(futures.size());
                    futures.forEach((server, future) -> {
                        try {
                            responses.put(server, future.join());
                        } catch (CompletionException ignoredException) {
                        }
                    });
                    return responses;
                });
    }

    public static CompletableFuture<Boolean> isServiceOnline(ServiceType service) {
        return cachedServiceStatus(service, System.currentTimeMillis(), () -> {
            CompletableFuture<Boolean> probe = new CompletableFuture<>();

            requestService(service, new PingProtocol(), new PingProtocol.EmptyMessage())
                    .thenAccept(response -> probe.complete(true))
                    .exceptionally(error -> {
                        probe.complete(false);
                        return null;
                    });

            TIMEOUTS.schedule(() -> probe.complete(false), 150, TimeUnit.MILLISECONDS);

            return probe;
        });
    }

    static CompletableFuture<Boolean> cachedServiceStatus(ServiceType service, long now,
                                                          Supplier<CompletableFuture<Boolean>> probe) {
        CachedServiceStatus cached = SERVICE_STATUS.get(service);
        if (cached != null && now - cached.checkedAt() < SERVICE_STATUS_TTL_MS) {
            return CompletableFuture.completedFuture(cached.online());
        }

        CompletableFuture<Boolean> result = new CompletableFuture<>();
        CompletableFuture<Boolean> inFlight = SERVICE_PROBES.putIfAbsent(service, result);
        if (inFlight != null) {
            return inFlight;
        }

        try {
            CompletableFuture<Boolean> probed = probe.get();
            if (probed == null) {
                completeServiceStatus(service, result, false, now);
            } else {
                probed.whenComplete((online, error) ->
                        completeServiceStatus(service, result, error == null && Boolean.TRUE.equals(online), now));
            }
        } catch (Exception exception) {
            completeServiceStatus(service, result, false, now);
        }

        return result;
    }

    private static void completeServiceStatus(ServiceType service, CompletableFuture<Boolean> result,
                                              boolean online, long checkedAt) {
        SERVICE_STATUS.put(service, new CachedServiceStatus(online, checkedAt));
        result.complete(online);
        SERVICE_PROBES.remove(service, result);
    }

    static void cacheServiceStatus(ServiceType service, boolean online, long checkedAt) {
        SERVICE_STATUS.put(service, new CachedServiceStatus(online, checkedAt));
    }

    static void clearServiceStatusCache() {
        SERVICE_STATUS.clear();
        SERVICE_PROBES.clear();
    }

    @SuppressWarnings("unchecked")
    public static <T, R> RedisProtocol<T, R> protocolFor(T request) {
        RedisProtocol<?, ?> protocol = protocolsByRequest.get(request.getClass().getSimpleName());
        if (protocol == null) {
            throw new IllegalArgumentException("No Redis protocol registered for " + request.getClass().getSimpleName());
        }
        return (RedisProtocol<T, R>) protocol;
    }

    private static <T, R> CompletableFuture<R> request(String target,
                                                       String requestChannel,
                                                       String responseChannel,
                                                       RedisProtocol<T, R> protocol,
                                                       T message) {
        return RedisMessageBus.request(localEndpoint(), target, requestChannel, responseChannel, protocol, message);
    }

    private static String requestTypeName(RedisProtocol<?, ?> protocol) {
        Type genericSuperclass = protocol.getClass().getGenericSuperclass();

        if (genericSuperclass instanceof ParameterizedType paramType) {
            Type[] typeArguments = paramType.getActualTypeArguments();
            if (typeArguments.length > 0) {
                Type firstTypeArg = typeArguments[0];
                if (firstTypeArg instanceof Class) {
                    return ((Class<?>) firstTypeArg).getSimpleName();
                }
                return firstTypeArg.getTypeName();
            }
        }

        throw new IllegalArgumentException("Could not determine request type for " + protocol.getClass().getName());
    }
}
