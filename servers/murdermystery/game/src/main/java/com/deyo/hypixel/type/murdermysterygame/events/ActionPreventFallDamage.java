package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.damage.DamageType;
import com.deyo.hypixel.pvp.events.FinalDamageEvent;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPreventFallDamage implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(FinalDamageEvent event) {
        if (!(event.getEntity() instanceof MurderMysteryPlayer player)) return;

        // Allow damage in creative mode
        if (player.getGameMode() == GameMode.CREATIVE) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;

        // Check if this is fall damage
        DamageType damageType = event.getDamage().getType().asValue();
        if (damageType == DamageType.FALL.asValue()) {
            event.setCancelled(true);
        }
    }
}
