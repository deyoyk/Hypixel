package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockMenu;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickItem implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(InventoryPreClickEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        ItemStack itemStack = event.getClickedItem();
        if (!SkyBlockItem.isSkyBlockItem(itemStack)) return;

        SkyBlockItem item = new SkyBlockItem(itemStack);

        if (item.getItemType() == ItemType.SKYBLOCK_MENU) {
            event.setCancelled(true);
            player.openView(new GUISkyBlockMenu());
        }
    }
}
