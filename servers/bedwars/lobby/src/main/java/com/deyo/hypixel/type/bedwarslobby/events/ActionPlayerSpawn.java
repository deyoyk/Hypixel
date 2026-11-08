package com.deyo.hypixel.type.bedwarslobby.events;

import lombok.SneakyThrows;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.bedwarslobby.TypeBedWarsLobbyLoader;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerSpawn implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL); //noticed stayed in Spectator after Bedwars game end. This is Hacky, but I don't see a reason why gamemode shouldn't be enforced on lobby join. -petethepossum

        TypeBedWarsLobbyLoader.bedWarsLobbyMap.sendMapData(player);
    }
}
