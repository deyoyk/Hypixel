package com.deyo.hypixel.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.player.PlayerMoveEvent;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.entity.drop.ItemPickup;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerItemPickup implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        DroppedItemEntityImpl.getDroppedItems().computeIfPresent(player, (unused, list) -> {
            list.forEach(item -> {
                if (item.isRemoved() || !item.isPickable()) return;
                if (!ItemPickup.isWithinRange(player, item)) return;

                SkyBlockItem dropped = item.getItem();
                int amount = dropped.getAmount();

                ItemPickup.sendCollectPacket(player, item, amount);
                ItemPickup.playPickupSound(player);

                ItemType type = dropped.getAttributeHandler().getPotentialType();
                if (player.canInsertItemIntoSacks(type, amount)) {
                    player.getSackItems().increase(type, amount);
                } else {
                    player.addAndUpdateItem(dropped);
                }
                item.remove();
            });
            return list;
        });
    }
}
