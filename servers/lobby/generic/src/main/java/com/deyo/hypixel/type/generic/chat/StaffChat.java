package com.deyo.hypixel.type.generic.chat;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.commons.protocol.objects.proxy.to.StaffChatProtocol;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StaffChat {

    private static final StaffChatProtocol PROTOCOL = new StaffChatProtocol();

    public static void sendMessage(HypixelPlayer sender, String message) {
        Text formatted = Text.of("<b>[STAFF] <rank:{}>{}<f>: {}",
                sender.getRank(), sender.getUsername(), message);
        broadcastViaProxy(formatted.serialize());
    }

    public static void sendNotification(String message) {
        broadcastViaProxy(Text.of("<b>[STAFF] <7>{}", message).serialize());
    }

    private static void broadcastViaProxy(String formattedMessage) {
        RedisClient.requestProxy(PROTOCOL, new StaffChatProtocol.Request("message", formattedMessage, null));
    }
}
