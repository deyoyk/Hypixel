package com.deyo.hypixel.type.murdermysterylobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.murdermystery.MurderMysteryGameType;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.lobby.LobbyOrchestratorConnector;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class GUIPlay extends HypixelInventoryGUI {

    private final MurderMysteryGameType type;

    public GUIPlay(MurderMysteryGameType type) {
        super("Play Murder Mystery", InventoryType.CHEST_4_ROW);
        this.type = type;
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        set(new GUIClickableItem(12) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.IRON_SWORD, """
                        <a>Murder Mystery {}
                        <7>Play a game of Murder Mystery {0}

                        <7>{}

                        <e>Click to play!""", type.getDisplayName(), lore());
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                player.closeInventory();

                if (LobbyOrchestratorConnector.isSearching(player.getUuid())) {
                    player.sendMessage("<c>You are already searching for a game!");
                    return;
                }

                LobbyOrchestratorConnector connector = new LobbyOrchestratorConnector(player);
                connector.sendToGame(ServerType.MURDER_MYSTERY_GAME, type.toString());
            }
        });

        set(new GUIClickableItem(14) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.OAK_SIGN, """
                        <a>Map Selector {}
                        <7>Pick which map you want to play from
                        <7>a list of available maps.

                        <e>Click to browse!""", type.getDisplayName());
            }

            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer player) {
                new GUIMapSelection(type).open(player);
            }
        });

        set(GUIClickableItem.getCloseItem(31));
        updateItemStacks(getInventory(), getPlayer());
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }

    private String lore() {
        return switch (type) {
            case CLASSIC -> """
                    One player is the Murderer!
                    Find out who it is before
                    they kill everyone. One player
                    is the Detective with a bow.""";
            case DOUBLE_UP -> """
                    Two Murderers hunt the innocents!
                    Two Detectives must stop them!
                    More chaos, more fun!""";
            case ASSASSINS -> """
                    Everyone has a target to eliminate!
                    Kill your target and inherit theirs.
                    Last one standing wins!""";
        };
    }
}
