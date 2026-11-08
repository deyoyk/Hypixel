package com.deyo.hypixel.type.skywarsgame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.skywarsgame.TypeSkywarsGameLoader;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGame;
import com.deyo.hypixel.type.skywarsgame.gui.GUISpectatorTeleporter;
import com.deyo.hypixel.type.skywarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.skywarsgame.user.SkywarsPlayer;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class SpectatorCompass extends SimpleInteractableItem {

    public SpectatorCompass() {
        super("spectator_compass");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPASS, 1, """
                <a>Teleporter <7>(Right Click)
                <7>Right-click to teleport to players!""").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        SkywarsPlayer player = (SkywarsPlayer) event.getPlayer();
        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game != null) {
            new GUISpectatorTeleporter(game).open(player);
        }
    }
}
