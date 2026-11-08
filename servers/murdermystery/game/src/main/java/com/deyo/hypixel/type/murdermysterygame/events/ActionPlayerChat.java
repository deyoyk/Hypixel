package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.event.player.PlayerChatEvent;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.chat.StaffChat;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointChatType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;

public class ActionPlayerChat implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerChatEvent event) {
        if (event.isCancelled()) return;
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();
        event.setCancelled(true);

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        String message = event.getRawMessage();
        Rank rank = player.getRank();

        // Sanitize message
        if (!rank.isStaff()) {
            message = message.replaceAll("[^\\x00-\\x7F]", "");
        }

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

        // Dead players can only talk to other dead players
        if (player.isEliminated() && game.getGameStatus() == GameStatus.IN_PROGRESS) {
            for (MurderMysteryPlayer gamePlayer : game.getPlayers()) {
                if (gamePlayer.isEliminated()) {
                    gamePlayer.sendMessage("<7>[DEAD] <f>{}: <7>{}", player.getUsername(), finalMessage);
                }
            }
            return;
        }

        // Normal chat
        for (MurderMysteryPlayer gamePlayer : game.getPlayers()) {
            gamePlayer.sendMessage("{}<f>{}: {}",
                    player.getRankPrefix(),
                    player.getUsername(),
                    finalMessage);
        }
    }
}
