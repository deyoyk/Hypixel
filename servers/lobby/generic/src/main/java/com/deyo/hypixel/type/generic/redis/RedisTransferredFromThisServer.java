package com.deyo.hypixel.type.generic.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.PlayerSwitchedProtocol;
import com.deyo.hypixel.proxyapi.ProxyPlayer;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RedisTransferredFromThisServer implements RedisMessageHandler<PlayerSwitchedProtocol.Request, PlayerSwitchedProtocol.Response> {
    @Override
    public RedisProtocol<PlayerSwitchedProtocol.Request, PlayerSwitchedProtocol.Response> protocol() {
        return new PlayerSwitchedProtocol();
    }

    @Override
    public PlayerSwitchedProtocol.Response handle(PlayerSwitchedProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        if (!ProxyPlayer.waitingForTransferComplete.containsKey(uuid)) {
            return new PlayerSwitchedProtocol.Response();
        }

        CompletableFuture.delayedExecutor(500, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    CompletableFuture<Void> future = ProxyPlayer.waitingForTransferComplete.get(uuid);
                    if (future != null) {
                        future.complete(null);
                    }
                    ProxyPlayer.waitingForTransferComplete.remove(uuid);
                });

        return new PlayerSwitchedProtocol.Response();
    }
}
