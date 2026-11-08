package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.event.inventory.InventoryItemChangeEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.game.GameStatus;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionGoldInventoryMove implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(InventoryItemChangeEvent event) {
        if (event.getInventory().getViewers().isEmpty()) return;
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getInventory().getViewers().stream().findFirst().orElse(null);
        if (player == null) return;

        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game == null) return;
        if (game.getGameStatus() != GameStatus.IN_PROGRESS) return;

        int totalGold = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getItemStack(i);
            if (stack.material() == Material.GOLD_INGOT) {
                totalGold += stack.amount();
                if (i != 8) {
                    player.getInventory().setItemStack(i, ItemStack.AIR);
                }
            }
        }

        if (totalGold > 0) {
            player.getInventory().setItemStack(8, ItemStack.of(Material.GOLD_INGOT, totalGold));
        } else {
            ItemStack slot8Item = player.getInventory().getItemStack(8);
            if (slot8Item.material() == Material.GOLD_INGOT) {
                player.getInventory().setItemStack(8, ItemStack.AIR);
            }
        }
    }
}

