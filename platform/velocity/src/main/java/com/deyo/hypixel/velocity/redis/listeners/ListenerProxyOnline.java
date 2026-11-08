package com.deyo.hypixel.velocity.redis.listeners;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.ProxyIsOnlineProtocol;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.UUID;

public class ListenerProxyOnline implements RedisMessageHandler<
        ProxyIsOnlineProtocol.Request,
        ProxyIsOnlineProtocol.Response> {

    @Override
    public RedisProtocol<ProxyIsOnlineProtocol.Request, ProxyIsOnlineProtocol.Response> protocol() {
        return new ProxyIsOnlineProtocol();
    }

    @Override
    public ProxyIsOnlineProtocol.Response handle(ProxyIsOnlineProtocol.Request message, RedisMessageContext context) {
        if (GameManager.getFromUUID(UUID.fromString(context.origin().id())) == null) {
            return new ProxyIsOnlineProtocol.Response(false, true, null);
        }
        return new ProxyIsOnlineProtocol.Response(true, true, null);
    }
}
