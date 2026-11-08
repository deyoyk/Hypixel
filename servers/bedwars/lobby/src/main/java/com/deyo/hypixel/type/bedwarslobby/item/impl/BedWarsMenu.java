package com.deyo.hypixel.type.bedwarslobby.item.impl;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarslobby.gui.cosmetics.GUIBedWarsMenuShop;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.item.LobbyItem;

public class BedWarsMenu extends LobbyItem {

    public BedWarsMenu() {
        super("bedwars_menu");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.named(Material.EMERALD, "<a>Bed Wars Menu & Shop <7>(Right Click)").build();
    }

    @Override
    public void onItemDrop(ItemDropEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (event instanceof CancellableEvent cancellableEvent) {
            cancellableEvent.setCancelled(true);
        }
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        player.openView(new GUIBedWarsMenuShop());
    }

}
