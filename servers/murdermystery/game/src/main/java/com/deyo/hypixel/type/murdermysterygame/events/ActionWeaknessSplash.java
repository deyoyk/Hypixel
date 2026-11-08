package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.event.entity.EntityPotionAddEvent;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.protection.WeaknessProtectionManager;
import com.deyo.hypixel.type.murdermysterygame.role.GameRole;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;

/**
 * Detects when the murderer is hit by a weakness splash potion.
 * When this happens, apply the "weakened" state to the murderer.
 */
public class ActionWeaknessSplash implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(EntityPotionAddEvent event) {
        if (!(event.getEntity() instanceof MurderMysteryPlayer player)) return;

        // Only react to weakness effect
        if (event.getPotion().effect() != PotionEffect.WEAKNESS) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null || game.getGameStatus() != GameStatus.IN_PROGRESS) return;

        // Check if this player is the murderer
        GameRole role = game.getRoleManager().getRole(player.getUuid());
        if (role == GameRole.MURDERER) {
            WeaknessProtectionManager.applyWeaknessToMurderer(player);
        }
    }
}
