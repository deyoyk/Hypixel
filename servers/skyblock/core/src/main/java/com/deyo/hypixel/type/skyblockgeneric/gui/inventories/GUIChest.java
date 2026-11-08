package com.deyo.hypixel.type.skyblockgeneric.gui.inventories;


import net.kyori.adventure.sound.Sound;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.chest.Chest;
import com.deyo.hypixel.type.skyblockgeneric.chest.ChestAnimationType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.stream.IntStream;

public class GUIChest extends HypixelInventoryGUI {

    private final Chest chest;

    public GUIChest(Chest chest) {
        super(Text.literal(chest.getName()), chest.getSize());
        this.chest = chest;
    }

    @Override
    public void setItems(InventoryGUIOpenEvent e) {
        IntStream.range(0, chest.getItems().size()).forEach(index -> {
            set(new GUIItem(index) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStacks.copy(chest.getItem(index));
                }

                @Override
                public boolean canPickup() {
                    return true;
                }
            });
        });

    }

    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        e.getPlayer().playSound(Sound.sound(SoundEvent.BLOCK_CHEST_CLOSE, Sound.Source.RECORD, 1f, 1f));
        ChestAnimationType.CLOSE.play(chest.getInstance(), chest.getPosition());

        AbstractInventory inventory = e.getInventory();
        IntStream.range(0, inventory.getItemStacks().length).forEach(i -> chest.setItem(i, inventory.getItemStack(i)));
    }

    @Override
    public boolean allowHotkeying() {
        return true;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
    }
}
