package com.deyo.hypixel.type.murdermysterylobby.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelConst;
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
        if (!(HypixelConst.getTypeLoader().getType() == ServerType.MURDER_MYSTERY_LOBBY)) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage("{} <6>joined the lobby!", player.getFullDisplayName());
        }
    }
}
