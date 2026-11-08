package com.deyo.hypixel.commons.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;

public interface RedisMessageHandler<T, R> {
    RedisProtocol<T, R> protocol();

    R handle(T message, RedisMessageContext context);
}
