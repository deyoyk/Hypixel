package com.deyo.hypixel.type.hub.gui;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class GUIJamie extends HypixelInventoryGUI {
    public GUIJamie() {
        super("Claim Reward", InventoryType.CHEST_6_ROW);
    }

    public void onOpen(InventoryGUIOpenEvent e) {
        fill(ItemStacks.filler(Material.BLACK_STAINED_GLASS_PANE));
        set(GUIClickableItem.getCloseItem(49));

        set(new GUIClickableItem(22) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                player.addAndUpdateItem(ItemType.ROGUE_SWORD);
                player.closeInventory();
                player.sendMessage("<a>You claimed a <f>Rogue Sword</f>!");
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                return ItemStacks.item(Material.GOLDEN_SWORD, """
                        <f>Rogue Sword
                        <7>Damage: <c>+20

                        <6>Ability: Speed Boost <e><l>RIGHT CLICK</l>
                        <7>Grants <f>+100✦ Speed </f>for
                        <a>30 seconds</a>.
                        <8>Mana Cost: <3>50

                        <8>This item can be reforged!
                        <f><l>COMMON SWORD</l>

                        <e>Click to claim!""");
            }
        });
        updateItemStacks(getInventory(), getPlayer());
    }
    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
    }

    @Override
    public void suddenlyQuit(Inventory inventory, HypixelPlayer player) {
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
