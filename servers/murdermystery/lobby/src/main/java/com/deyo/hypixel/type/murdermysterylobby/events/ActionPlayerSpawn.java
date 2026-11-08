package com.deyo.hypixel.type.murdermysterylobby.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import net.minestom.server.entity.GameMode;
import com.deyo.hypixel.type.murdermysterylobby.TypeMurderMysteryLobbyLoader;
import com.deyo.hypixel.type.murdermysterylobby.hologram.LeaderboardHologramManager;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.SPAWN)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);

        // Spawn leaderboard holograms for this player
        LeaderboardHologramManager.spawnHologramsForPlayer(player);
        TypeMurderMysteryLobbyLoader.lobbyMap.sendMapData(player);
    }
}
