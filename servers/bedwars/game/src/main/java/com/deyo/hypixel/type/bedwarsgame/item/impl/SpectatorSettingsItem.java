package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.gui.GUIBedWarsSpectatorSettings;
import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class SpectatorSettingsItem extends SimpleInteractableItem {

    public SpectatorSettingsItem() {
        super("spectator_settings");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPARATOR, """
                <b><l>Spectator Settings </l><7>(Right Click)
                <7>Right-click to change your spectator settings!""").build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (!(event.getPlayer() instanceof BedWarsPlayer player)) return;
        player.openView(new GUIBedWarsSpectatorSettings());
    }
}
