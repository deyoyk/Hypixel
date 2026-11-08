package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.gui.GUITrackerAndCommunication;
import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class CompassItem extends SimpleInteractableItem {

    public CompassItem() {
        super("compass");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.named(Material.COMPASS, Text.key("bedwars.item.compass")).build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.openView(new GUITrackerAndCommunication());
    }
}
