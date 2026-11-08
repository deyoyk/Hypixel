package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;

/**
 * Kills players who fall into water with a drowning death message.
 */
public class ActionWaterDeath implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerMoveEvent event) {
        if (!(event.getPlayer() instanceof MurderMysteryPlayer player)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

        // Already eliminated
        if (player.isEliminated()) return;

        // Check block at player's feet position
        Pos pos = player.getPosition();
        Block blockAtFeet = game.getInstanceContainer().getBlock(pos.blockX(), pos.blockY(), pos.blockZ());

        // Check if player is in water
        if (blockAtFeet == Block.WATER) {
            game.onEnvironmentalDeath(player, "You drowned.");
        }
    }
}
