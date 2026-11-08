package com.deyo.hypixel.type.ravengarddungeon.events;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.game.game.Game;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;
import com.deyo.hypixel.type.ravengarddungeon.TypeRavengardDungeonLoader;
import com.deyo.hypixel.type.ravengarddungeon.user.RavengardDungeonPlayer;

public class ActionPlayerJoin implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, phase = EventPhase.GAMEPLAY)
    public void run(AsyncPlayerConfigurationEvent event) {
        RavengardDungeonPlayer player = (RavengardDungeonPlayer) event.getPlayer();
        ScheduleUtility.delay(() -> {
            if (!player.isOnline()) return;
            for (var instance : com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.all()) {
                if (instance.getPlayers().contains(player.getUuid())) {
                    return;
                }
            }
            Game.JoinResult result = TypeRavengardDungeonLoader.getGame().join(player);
            if (result instanceof Game.JoinResult.Denied(String reason)) {
                player.sendMessage("<c>Could not enter the dungeon: {}", reason);
                player.sendTo(ServerType.RAVENGARD_LOBBY);
            }
        }, 20);
    }
}
