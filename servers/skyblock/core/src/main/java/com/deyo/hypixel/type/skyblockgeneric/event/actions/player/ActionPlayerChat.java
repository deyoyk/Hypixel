package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerChatEvent;
import com.deyo.hypixel.commons.ServerType;
import net.kyori.adventure.text.format.NamedTextColor;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.chat.StaffChat;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointChatType;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.party.PartyManager;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class ActionPlayerChat implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        event.setCancelled(true);

        SkyBlockDataHandler dataHandler = player.getSkyblockDataHandler();
        if (dataHandler == null) return;

        String message = event.getRawMessage();
        Rank rank = player.getRank();

        // Sanitize message to remove any special Unicode characters
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

        List<SkyBlockPlayer> receivers = SkyBlockGenericLoader.getLoadedPlayers();

        receivers.removeIf(receiver -> {
            return HypixelConst.getTypeLoader().getType() == ServerType.SKYBLOCK_ISLAND &&
                    !receiver.getInstance().equals(player.getInstance());
        });

        receivers.forEach(onlinePlayer -> {
            boolean showLevel = onlinePlayer.getToggles().get(DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT);

            Text namePrefix = showLevel
                    ? player.getFullDisplayName()
                    : Text.of("{}", player.getRank().displayName(player.getRankColor(), player.isMvpPlusPlusAqua() ? NamedTextColor.AQUA : NamedTextColor.GOLD, player.getUsername()));

            if (rank.equals(Rank.DEFAULT))
                onlinePlayer.sendMessage(namePrefix.append("<7>: ").append(Text.literal(finalMessage)));
            else
                onlinePlayer.sendMessage(namePrefix.append("<f>: ").append(Text.literal(finalMessage)));
        });
    }
}
