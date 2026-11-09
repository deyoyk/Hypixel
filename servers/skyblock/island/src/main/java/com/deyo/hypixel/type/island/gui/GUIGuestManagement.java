package com.deyo.hypixel.type.island.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.island.tab.IslandGuestsModule;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.UUID;

public class GUIGuestManagement extends HypixelInventoryGUI {
    private static final int[] GUEST_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34
    };

    public GUIGuestManagement() {
        super("Guests Management", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(48, new GUIGuests()));
        set(GUIClickableItem.getCloseItem(49));

        SkyBlockPlayer player = (SkyBlockPlayer) e.player();
        var island = player.getSkyBlockIsland();
        boolean allowed = island != null && canManage(player.getUuid(),
                player.getProfiles().getCurrentlySelected(), island.getCoop(), island.getIslandID());
        if (!allowed) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.item(Material.BARRIER, 1, "<c>Guests Management", List.of(
                            Text.of("<7>Only island members can manage guests.")));
                }
            });
            updateItemStacks(getInventory(), getPlayer());
            return;
        }

        List<SkyBlockPlayer> guests = IslandGuestsModule.guests(player);
        if (guests.isEmpty()) {
            set(new GUIItem(22) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.item(Material.LIGHT_GRAY_DYE, 1, "<7>No online guests", List.of(
                            Text.of("<7>Your island has no guests right now.")));
                }
            });
        }

        for (int index = 0; index < guests.size() && index < GUEST_SLOTS.length; index++) {
            SkyBlockPlayer guest = guests.get(index);
            set(new GUIClickableItem(GUEST_SLOTS[index]) {
                @Override
                public void run(InventoryPreClickEvent click, HypixelPlayer clicker) {
                    guest.sendTo(ServerType.SKYBLOCK_HUB);
                    player.sendMessage("<a>Kicked <e>{} <a>from your island!", guest.getUsername());
                    new GUIGuestManagement().open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.head(guest.getPlayerSkin(), 1, guest.getUsername(), List.of(
                            Text.of("<7>Click to kick this guest from"),
                            Text.of("<7>your island.")));
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    static boolean canManage(UUID playerId, UUID profileId, CoopDatabase.Coop coop, UUID islandId) {
        if (coop != null) {
            return coop.isOriginator(playerId) || coop.members().contains(playerId);
        }
        return islandId != null && islandId.equals(profileId);
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
