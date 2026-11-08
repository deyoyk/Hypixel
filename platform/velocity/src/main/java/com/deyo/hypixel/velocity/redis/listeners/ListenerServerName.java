package com.deyo.hypixel.velocity.redis.listeners;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.RequestServerNameProtocol;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.UUID;

public class ListenerServerName implements RedisMessageHandler<
        RequestServerNameProtocol.Request,
        RequestServerNameProtocol.Response> {

    @Override
    public RedisProtocol<RequestServerNameProtocol.Request, RequestServerNameProtocol.Response> protocol() {
        return new RequestServerNameProtocol();
    }

    @Override
    public RequestServerNameProtocol.Response handle(RequestServerNameProtocol.Request message, RedisMessageContext context) {
        GameManager.GameServer server = GameManager.getFromUUID(UUID.fromString(context.origin().id()));
        if (server == null) {
            return new RequestServerNameProtocol.Response(null, null, false,
                    "Server " + context.origin().id() + " is not registered with the proxy");
        }

        return new RequestServerNameProtocol.Response(server.displayName(), server.shortDisplayName(), true, null);
    }
}
