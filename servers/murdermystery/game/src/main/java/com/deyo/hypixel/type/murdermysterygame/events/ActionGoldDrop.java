package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionGoldDrop implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(ItemDropEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }

        Material material = event.getItemStack().material();

        if (material == Material.GOLD_INGOT) {
            event.setCancelled(true);
            return;
        }

        // Prevent dropping weapons
        if (material == Material.BOW || material == Material.ARROW || material == Material.IRON_SWORD) {
            event.setCancelled(true);
        }
    }
}
