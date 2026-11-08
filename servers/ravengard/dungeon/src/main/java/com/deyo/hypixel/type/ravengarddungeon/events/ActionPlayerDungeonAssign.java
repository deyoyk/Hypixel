package com.deyo.hypixel.type.ravengarddungeon.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.redis.service.GameInformationHandler;
import com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry;
import com.deyo.hypixel.type.ravengarddungeon.user.RavengardDungeonPlayer;

import java.util.UUID;

public class ActionPlayerDungeonAssign implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onSpawn(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn() || !(event.getPlayer() instanceof RavengardDungeonPlayer player)) {
            return;
        }
        String assignedGameId = GameInformationHandler.game.remove(player.getUuid());
        if (assignedGameId == null) {
            return;
        }
        DungeonInstanceRegistry.DungeonInstance instance;
        try {
            instance = DungeonInstanceRegistry.get(UUID.fromString(assignedGameId));
        } catch (IllegalArgumentException exception) {
            return;
        }
        if (instance == null) {
            player.sendMessage("<c>Your dungeon instance no longer exists.");
            return;
        }
        DungeonInstanceRegistry.sendPlayerIn(player, instance);
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT)
    public void onDisconnect(PlayerDisconnectEvent event) {
        UUID uuid = event.getPlayer().getUuid();
        for (DungeonInstanceRegistry.DungeonInstance instance : DungeonInstanceRegistry.all()) {
            instance.markPlayerLeft(uuid);
        }
    }
}
