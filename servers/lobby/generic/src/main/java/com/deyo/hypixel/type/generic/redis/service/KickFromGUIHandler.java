package com.deyo.hypixel.type.generic.redis.service;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.gui.KickFromGUIPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.gui.KickFromGUIPushProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.gui.KickFromGUIPushProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class KickFromGUIHandler implements RedisMessageHandler<Request, Response> {

    private static final KickFromGUIPushProtocol PROTOCOL = new KickFromGUIPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
        List<UUID> kickedPlayers = new ArrayList<>();

        for (UUID playerUUID : message.playerUUIDs()) {
            HypixelPlayer player = HypixelGenericLoader.getFromUUID(playerUUID);

            if (player != null && HypixelInventoryGUI.GUI_MAP.containsKey(playerUUID)) {
                HypixelInventoryGUI gui = HypixelInventoryGUI.GUI_MAP.get(playerUUID);

                if (gui.getClass().getSimpleName().toLowerCase().contains(message.guiType().toLowerCase())) {
                    player.closeInventory();
                    player.sendMessage("<c>You have been kicked from the {} GUI due to a data synchronization operation.", message.guiType());
                    kickedPlayers.add(playerUUID);
                }
            }
        }

        return new Response(true, kickedPlayers, kickedPlayers.size(), null);
    }
}
