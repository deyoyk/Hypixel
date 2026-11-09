package com.deyo.hypixel.type.generic.redis.service;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.messaging.SendMessagePushProtocol;
import com.deyo.hypixel.commons.protocol.objects.messaging.SendMessagePushProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.messaging.SendMessagePushProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class SendMessageHandler implements RedisMessageHandler<Request, Response> {

    private static final SendMessagePushProtocol PROTOCOL = new SendMessagePushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
        try {
            HypixelPlayer player = HypixelGenericLoader.getFromUUID(message.playerUUID());

            if (player != null) {
                String raw = message.message();
                player.sendMessage(raw);
                return new Response(true, null);
            }

            return new Response(false, "Player not found or error");
        } catch (Exception e) {
            return new Response(false, "Player not found or error");
        }
    }
}
