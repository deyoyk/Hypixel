package com.deyo.hypixel.type.skywarsgame.events;

import net.minestom.server.event.player.PlayerChatEvent;
import com.deyo.hypixel.commons.skywars.SkyWarsLevelColor;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.chat.StaffChat;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBoolean;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointChatType;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLong;
import com.deyo.hypixel.type.generic.data.handlers.SkywarsDataHandler;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.party.PartyManager;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skywarsgame.TypeSkywarsGameLoader;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGame;
import com.deyo.hypixel.type.skywarsgame.user.SkywarsPlayer;
import com.deyo.hypixel.type.skywarslobby.level.SkywarsLevelRegistry;

public class ActionPlayerChat implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        final SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        event.setCancelled(true);

        if (player.getDataHandler() == null) return;

        SkywarsDataHandler skywarsDataHandler = SkywarsDataHandler.getUser(player);
        if (skywarsDataHandler == null) {
            player.sendMessage("<c>An error occurred while processing your chat message. Please try again later.");
            return;
        }

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

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game == null) return;

        boolean hideLevel = skywarsDataHandler.get(SkywarsDataHandler.Data.HIDE_LEVEL, DatapointBoolean.class).getValue();
        Text levelPrefix;
        if (!hideLevel) {
            int level = SkywarsLevelRegistry.calculateLevel(
                    skywarsDataHandler.get(SkywarsDataHandler.Data.EXPERIENCE, DatapointLong.class).getValue()
            );
            levelPrefix = Text.of("{} ", SkyWarsLevelColor.getLevelDisplay(level));
        } else {
            levelPrefix = Text.empty();
        }

        Text speaker = levelPrefix.append(Text.of("{}{}", player.getRankPrefix(),
                player.getUsername()));
        Text line = Text.of(rank.equals(Rank.DEFAULT) ? "{}<7>: {}" : "{}<f>: {}",
                speaker, finalMessage);

        for (SkywarsPlayer gamePlayer : game.getPlayers()) {
            gamePlayer.sendMessage(line);
        }
    }
}
