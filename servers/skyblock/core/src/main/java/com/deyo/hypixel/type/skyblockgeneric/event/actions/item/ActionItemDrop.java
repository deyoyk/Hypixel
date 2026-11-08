package com.deyo.hypixel.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.ItemDropEvent;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.entity.drop.ItemDrops;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemDrop implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(ItemDropEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (new SkyBlockItem(event.getItemStack()).getAttributeHandler().getTypeAsString().toLowerCase().contains("menu")) {
            event.setCancelled(true);
            return;
        }

        if (player.getOpenInventory() != null) {
            event.setCancelled(true);
            return;
        }

        if (player.isInLaunchpad()) {
            event.setCancelled(true);
            return;
        }

        boolean hideMessage = player.getToggles().get(DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES);

        if (!hideMessage) {
            player.sendMessage("<hover:'<e>Click here to disable the alert!'><click:run:'/toggledropalert'><e>⚠ <a>Your drops can't be seen by other players in <b>SkyBlock<a>!</click></hover>");
            player.sendMessage("<hover:'<e>Click here to disable the alert!'><click:run:'/toggledropalert'><a>Only you can pickup your dropped items!</click></hover>");
            player.sendMessage("<hover:'<e>Click here to disable the alert!'><click:run:'/toggledropalert'><e>Click here to disable this alert forever!</click></hover>");
        }

        ItemDrops.throwFromPlayer(player, new DroppedItemEntityImpl(
                new SkyBlockItem(event.getItemStack()), player));
    }
}
