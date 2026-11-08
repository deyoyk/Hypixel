package com.deyo.hypixel.type.generic.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.PingServerProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RedisPing implements RedisMessageHandler<PingServerProtocol.Request, PingServerProtocol.Response> {
    @Override
    public RedisProtocol<PingServerProtocol.Request, PingServerProtocol.Response> protocol() {
        return new PingServerProtocol();
    }

    @Override
    public PingServerProtocol.Response handle(PingServerProtocol.Request message, RedisMessageContext context) {
        return new PingServerProtocol.Response();
    }
}
