package com.deyo.hypixel.type.prototypelobby.events;

import net.minestom.server.event.player.PlayerChatEvent;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.chat.StaffChat;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointChatType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.party.PartyManager;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

import java.util.List;

public class ActionPlayerChat implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        event.setCancelled(true);

        HypixelDataHandler dataHandler = player.getDataHandler();
        if (dataHandler == null) return;

        String message = event.getRawMessage();
        Rank rank = player.getRank();

        if (!rank.isStaff())
            message = message.replaceAll("[^\\x00-\\x7F]", "");

        String finalMessage = message;

        DatapointChatType.Chats chatType = player.getChatType().currentChatType;
        if (chatType == DatapointChatType.Chats.STAFF) {
            if (!rank.isStaff()) {
                player.sendMessage("<c>Unknown chat type.");
                player.getChatType().switchTo(DatapointChatType.Chats.ALL);
                return;
            }
            StaffChat.sendMessage(player, finalMessage);
            return;
        }

        if (chatType == DatapointChatType.Chats.PARTY) {
            if (!PartyManager.isInParty(player)) {
                player.sendMessage("<c>You are not in a party and were moved to the ALL channel.");
                player.getChatType().switchTo(DatapointChatType.Chats.ALL);
                return;
            }

            PartyManager.sendChat(player, message);
            return;
        }

        List<HypixelPlayer> receivers = HypixelGenericLoader.getLoadedPlayers();

        receivers.forEach(onlinePlayer -> {
            if (rank.equals(Rank.DEFAULT))
                onlinePlayer.sendMessage("{}<7>: {}", player.getFullDisplayName(), finalMessage);
            else
                onlinePlayer.sendMessage("{}<f>: {}", player.getFullDisplayName(), finalMessage);
        });
    }
}
