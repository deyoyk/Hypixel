package com.deyo.hypixel.velocity.redis.listeners;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.BroadcastStaffChatProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.StaffChatProtocol;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisClient;

import java.util.UUID;

public class ListenerStaffChat implements RedisMessageHandler<
        StaffChatProtocol.Request,
        StaffChatProtocol.Response> {

    @Override
    public RedisProtocol<StaffChatProtocol.Request, StaffChatProtocol.Response> protocol() {
        return new StaffChatProtocol();
    }

    @Override
    public StaffChatProtocol.Response handle(StaffChatProtocol.Request message, RedisMessageContext context) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                message.type(), message.formattedMessage(), message.uuid()));
        return new StaffChatProtocol.Response();
    }

    public static void broadcastToAllServers(BroadcastStaffChatProtocol.Request message) {
        GameManager.getServers().forEach((serverType, serverList) -> {
            serverList.forEach(gameServer -> {
                RedisClient.requestServer(
                        gameServer.internalID(),
                        new BroadcastStaffChatProtocol(),
                        message
                );
            });
        });
    }

    public static void broadcastStaffJoin(UUID playerUuid) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                "join", null, playerUuid.toString()));
    }

    public static void broadcastStaffLeave(UUID playerUuid) {
        broadcastToAllServers(new BroadcastStaffChatProtocol.Request(
                "leave", null, playerUuid.toString()));
    }
}
