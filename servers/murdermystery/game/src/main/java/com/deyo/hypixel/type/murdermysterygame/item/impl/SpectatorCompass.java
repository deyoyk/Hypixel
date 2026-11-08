package com.deyo.hypixel.type.murdermysterygame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.gui.GUITeleporter;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.murdermysterygame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class SpectatorCompass extends SimpleInteractableItem {

    public SpectatorCompass() {
        super("spectator_compass");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPASS, """
                <a>Teleporter <7>(Right Click)
                <7>Right-click to teleport to players!""").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();
        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            new GUITeleporter(game).open(player);
        }
    }
}
