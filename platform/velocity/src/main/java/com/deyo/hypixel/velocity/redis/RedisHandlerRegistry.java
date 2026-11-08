package com.deyo.hypixel.velocity.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.redis.RedisChannels;
import com.deyo.hypixel.commons.redis.RedisEndpoint;
import com.deyo.hypixel.commons.redis.RedisMessageBus;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisHandlerRegistry {

    public static <T, R> void register(RedisMessageHandler<T, R> handler) {
        RedisProtocol<T, R> protocol = handler.protocol();

        RedisMessageBus.registerHandler(
                RedisEndpoint.proxy(),
                RedisChannels.protocol(protocol),
                handler,
                (envelope, channel) -> RedisMessageContext.between(
                        UUID.fromString(envelope.id()),
                        RedisEndpoint.server(envelope.from()),
                        RedisEndpoint.proxy(),
                        protocol.channel()
                ),
                envelope -> envelope.from(),
                envelope -> RedisChannels.protocol(protocol)
        );
    }
}
