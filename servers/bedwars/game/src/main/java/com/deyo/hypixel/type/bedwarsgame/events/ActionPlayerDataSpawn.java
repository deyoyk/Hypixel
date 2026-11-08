package com.deyo.hypixel.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.bedwars.BedwarsLevelUtil;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLeaderboardLong;
import com.deyo.hypixel.type.generic.data.handlers.BedWarsDataHandler;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        BedWarsDataHandler handler = BedWarsDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);

        DatapointLeaderboardLong dp = handler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class);
        player.setLevel(BedwarsLevelUtil.calculateLevel(dp.getValue()));
        player.setExp((float) BedwarsLevelUtil.calculateExperienceSinceLastLevel(dp.getValue()) / BedwarsLevelUtil.calculateMaxExperienceFromExperience(dp.getValue()));

        // should be handled in BedWarsPlayer
        //List<HypixelPlayer> players = HypixelGenericLoader.getLoadedPlayers();
        //Collection<Player> playerCollection = players.stream().map(p -> (Player) p).toList();
        //PacketSendingUtils.sendGroupedPacket(playerCollection, new PlayerInfoRemovePacket(player.getUuid()));
    }
}