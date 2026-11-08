package com.deyo.hypixel.velocity.redis.listeners;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.RegisterServerProtocol;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.UUID;

public class ListenerServerInitialized implements RedisMessageHandler<
        RegisterServerProtocol.Request,
        RegisterServerProtocol.Response> {

    @Override
    public RedisProtocol<RegisterServerProtocol.Request, RegisterServerProtocol.Response> protocol() {
        return new RegisterServerProtocol();
    }

    @Override
    public RegisterServerProtocol.Response handle(RegisterServerProtocol.Request message, RedisMessageContext context) {
        ServerType type = ServerType.valueOf(message.type());
        int port = message.port() != null ? message.port() : -1;

        String host = message.host() != null
                ? message.host()
                : ConfigProvider.settings().getHostName();

        int maxPlayers = message.maxPlayers();

        UUID serverID = UUID.fromString(context.origin().id());
        GameManager.GameServer server = GameManager.getFromUUID(serverID);
        if (server == null) {
            server = GameManager.addServer(type, serverID, host, port, maxPlayers);
        }

        return new RegisterServerProtocol.Response(
                server.registeredServer().getServerInfo().getAddress().getHostString(),
                server.registeredServer().getServerInfo().getAddress().getPort(),
                true,
                null);
    }
}
