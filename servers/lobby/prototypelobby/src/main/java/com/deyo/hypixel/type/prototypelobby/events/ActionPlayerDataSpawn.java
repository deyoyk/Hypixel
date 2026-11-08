package com.deyo.hypixel.type.prototypelobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        Text joinMessage = Text.of("{} <6>joined the lobby!", player.getFullDisplayName());
        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage(joinMessage);
        }

        player.sendMessage("");
        player.sendMessage("<f>➔ <6><l>Welcome to the Prototype Lobby");
        player.sendMessage("<a>All games in this lobby are currently in development.");
        player.sendMessage("<click:url:'https://hypixel.net/PTL'><e>Click here to leave feedback! <f>➤ <b><n>https://hypixel.net/PTL");
        player.sendMessage("");
    }
}
