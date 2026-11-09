package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.visibility.PlayerVisibilityManager;

public class GUISettings extends HypixelInventoryGUI {

    public GUISettings() {
        super("Settings & Visibility", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(31, new GUIMyProfile()));
        set(GUIClickableItem.getCloseItem(35));

        set(new GUIClickableItem(13) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                boolean visible = player.getToggles().get(DatapointToggles.Toggles.ToggleType.LOBBY_SHOW_PLAYERS);
                return ItemStacks.item(visible ? Material.LIME_DYE : Material.GRAY_DYE, """
                        <a>Player Visibility
                        <7>Current: {}

                        <7>Toggle whether other players are
                        <7>visible while in lobbies.

                        <e>Click to toggle!""", visibilityLabel(visible));
            }

            @Override
            public void run(InventoryPreClickEvent click, HypixelPlayer player) {
                boolean visible = PlayerVisibilityManager.toggleVisibility(player);
                player.sendMessage("<7>Players are now {}.", visibilityLabel(visible));
                new GUISettings().open(player);
            }
        });

        updateItemStacks(getInventory(), getPlayer());
    }

    static String visibilityLabel(boolean visible) {
        return visible ? "<a>Shown" : "<c>Hidden";
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
