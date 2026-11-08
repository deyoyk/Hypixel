package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPreventBlockModification implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockBreak(PlayerBlockBreakEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        // Allow in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            event.setCancelled(true);
        }
    }
}
