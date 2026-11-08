package com.deyo.hypixel.service.generic.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.PingProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

public class PingEndpoint implements RedisMessageHandler<
        PingProtocol.EmptyMessage,
        PingProtocol.EmptyMessage> {
    @Override
    public RedisProtocol<PingProtocol.EmptyMessage, PingProtocol.EmptyMessage> protocol() {
        return new PingProtocol();
    }

    @Override
    public PingProtocol.EmptyMessage handle(PingProtocol.EmptyMessage messageObject, RedisMessageContext context) {
        return new PingProtocol.EmptyMessage();
    }
}
