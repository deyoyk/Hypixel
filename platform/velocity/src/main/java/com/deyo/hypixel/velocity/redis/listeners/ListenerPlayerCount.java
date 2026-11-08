package com.deyo.hypixel.velocity.redis.listeners;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.PlayerCountProtocol;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;
import java.util.UUID;

public class ListenerPlayerCount implements RedisMessageHandler<
        PlayerCountProtocol.Request,
        PlayerCountProtocol.Response> {

    @Override
    public RedisProtocol<PlayerCountProtocol.Request, PlayerCountProtocol.Response> protocol() {
        return new PlayerCountProtocol();
    }

    @Override
    public PlayerCountProtocol.Response handle(PlayerCountProtocol.Request message, RedisMessageContext context) {
        switch (message.lookupType()) {
            case ALL -> {
                int count = GameManager.getServers().values().stream()
                        .flatMap(List::stream)
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum();
                return new PlayerCountProtocol.Response(count, true, null);
            }
            case TYPE -> {
                ServerType serverType = ServerType.valueOf(message.lookupValue());
                int count = GameManager.getServers().entrySet().stream()
                        .filter(entry -> entry.getKey() == serverType)
                        .flatMap(entry -> entry.getValue().stream())
                        .mapToInt(server -> server.registeredServer().getPlayersConnected().size())
                        .sum();
                return new PlayerCountProtocol.Response(count, true, null);
            }
            case UUID -> {
                UUID uuid = UUID.fromString(message.lookupValue());
                int count = GameManager.getFromUUID(uuid).registeredServer().getPlayersConnected().size();
                return new PlayerCountProtocol.Response(count, true, null);
            }
        }
        throw new RuntimeException("Unknown lookup type: " + message.lookupType());
    }
}
