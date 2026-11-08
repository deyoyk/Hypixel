package com.deyo.hypixel.service.generic;

import lombok.RequiredArgsConstructor;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.redis.RedisChannels;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.redis.RedisEndpoint;
import com.deyo.hypixel.commons.redis.RedisEnvelope;
import com.deyo.hypixel.commons.redis.RedisMessageBus;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.libs.redisapi.api.RedisAPI;
import com.deyo.hypixel.service.generic.redis.PingEndpoint;
import com.deyo.hypixel.service.generic.redis.ServiceRedisManager;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@RequiredArgsConstructor
public class ServiceInitializer {
    private final SkyBlockService service;

    public void init() {
        Logger.info("Initializing service {}...", service.getType().name());
        ItemAttribute.registerItemAttributes();

        ServiceRedisManager.connect(ConfigProvider.settings().getRedisUri(), service.getType());
        RedisClient.registerResponseChannel(RedisChannels.SERVICE_RESPONSE);
        RedisClient.registerResponseChannel(RedisChannels.SERVICE_BROADCAST_RESPONSE);

        List<RedisMessageHandler> endpoints = new ArrayList<>(service.getEndpoints());
        endpoints.add(new PingEndpoint());

        endpoints.forEach(endpoint -> {
            RedisProtocol<?, ?> protocolObject = endpoint.protocol();
            Logger.debug("Registering channel {}", protocolObject.channel());

            RedisMessageBus.registerHandler(
                RedisEndpoint.service(service.getType()),
                RedisChannels.protocol(protocolObject),
                endpoint,
                (envelope, _) -> RedisMessageContext.between(
                    UUID.fromString(envelope.id()),
                    RedisEndpoint.server(envelope.from()),
                    RedisEndpoint.service(service.getType()),
                    protocolObject.channel()
                ),
                RedisEnvelope::from,
                _ -> RedisChannels.protocol(protocolObject)
            );
        });

        RedisAPI.getInstance().startListeners();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                RedisAPI.getInstance().shutdown();
            } catch (Exception exception) {
                Logger.error(exception, "Failed to shut down Redis for service {}", service.getType().name());
            }
        }, "service-redis-shutdown"));

        Logger.info("Service {} initialized!", service.getType().name());
        service.onReady();

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
