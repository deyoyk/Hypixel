package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class PlayAgainItem extends SimpleInteractableItem {


    public PlayAgainItem() {
        super("play_again");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPASS, """
                <b><l>Play Again </l><7>(Right Click)
                <7>Right-click to play another game!""").build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.sendMessage("<click:url:'https://github.com/deyo/Hypixel-v2'><c>This Feature is not there yet. <a>Open a Pull request HERE to get it added quickly!");
    }
}
