package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.item.Material;
import com.deyo.hypixel.pvp.events.PrepareAttackEvent;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.protection.WeaknessProtectionManager;
import com.deyo.hypixel.type.murdermysterygame.role.GameRole;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPlayerCombat implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PrepareAttackEvent event) {
        if (!(event.getEntity() instanceof MurderMysteryPlayer attacker)) return;
        if (!(event.getTarget() instanceof MurderMysteryPlayer victim)) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(attacker);
        if (game == null) {
            event.setCancelled(true);
            return;
        }

        // No combat in waiting lobby
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) {
            event.setCancelled(true);
            return;
        }

        // Check if victim is already eliminated
        if (victim.isEliminated()) {
            event.setCancelled(true);
            return;
        }

        // Check if attacker is eliminated (spectator)
        if (attacker.isEliminated()) {
            event.setCancelled(true);
            return;
        }

        GameRole attackerRole = game.getRoleManager().getRole(attacker.getUuid());
        if (attackerRole == null) {
            event.setCancelled(true);
            return;
        }

        // Only murderer with knife can melee kill
        if (attackerRole == GameRole.MURDERER) {
            // Check if holding knife (iron sword) and murderer has received their sword
            Material heldItem = attacker.getItemInMainHand().material();
            if (heldItem == Material.IRON_SWORD && game.hasMurdererReceivedSword()) {
                // Check if murderer is weakened (from weakness splash potion)
                if (!WeaknessProtectionManager.handleMurdererAttack(attacker)) {
                    // Murderer is weakened - attack absorbed
                    attacker.sendMessage("<e>Your attack was weakened!");
                    event.setCancelled(true);
                    return;
                }
                // Kill the victim
                game.onPlayerKill(attacker, victim);
            }
            // Cancel the attack either way - murderer either kills instantly or does nothing
            event.setCancelled(true);
        } else {
            // Innocents and detectives cannot melee kill - they use bow
            // Cancel regular melee attacks
            event.setCancelled(true);
        }
    }
}
