package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;

public class GUIMuseumRewards extends HypixelInventoryGUI {
    public GUIMuseumRewards() {
        super(Text.key("gui_museum.rewards.title"), InventoryType.CHEST_6_ROW);
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {

    }
}
