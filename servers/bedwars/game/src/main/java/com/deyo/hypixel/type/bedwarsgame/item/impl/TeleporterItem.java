package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.gui.GUIBedWarsTeleporter;
import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class TeleporterItem extends SimpleInteractableItem {

    public TeleporterItem() {
        super("teleporter");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPASS, """
                <a><l>Teleporter </l><7>(Right Click)
                <7>Right-click to spectate players!""").build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (!(event.getPlayer() instanceof BedWarsPlayer player)) return;

        BedWarsGame game = player.getGame();
        if (game == null) return;

        player.openView(new GUIBedWarsTeleporter(game.getPlayers()));
    }
}
