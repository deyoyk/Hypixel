package com.deyo.hypixel.type.skywarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skywarslobby.hologram.LeaderboardHologramManager;
import com.deyo.hypixel.type.skywarslobby.hologram.SoulWellHologramManager;
import com.deyo.hypixel.type.skywarslobby.util.SkyWarsLobbyMap;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.SPAWN)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Send map data to player
        SkyWarsLobbyMap skywarsLobbyMap = new SkyWarsLobbyMap();
        skywarsLobbyMap.sendMapData(player);

        // Spawn leaderboard holograms for this player
        LeaderboardHologramManager.spawnHologramsForPlayer(player);

        // Spawn Soul Well hologram for this player
        SoulWellHologramManager.spawnHologramForPlayer(player);
    }
}
