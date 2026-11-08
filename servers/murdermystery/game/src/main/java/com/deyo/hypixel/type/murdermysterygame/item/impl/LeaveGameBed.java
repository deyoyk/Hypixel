package com.deyo.hypixel.type.murdermysterygame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.murdermysterygame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class LeaveGameBed extends SimpleInteractableItem {

    public LeaveGameBed() {
        super("leave_game");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.RED_BED, """
                <c><l>Return to Lobby </l><7>(Right Click)
                <7>Right-click to leave to the lobby!""").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        Game game = TypeMurderMysteryGameLoader.getPlayerGame(event.getPlayer());
        if (game != null) {
            game.leave((MurderMysteryPlayer) event.getPlayer());
        }
    }
}
