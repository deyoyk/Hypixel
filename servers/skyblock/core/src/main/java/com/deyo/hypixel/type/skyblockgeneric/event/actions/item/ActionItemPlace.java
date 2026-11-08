package com.deyo.hypixel.type.skyblockgeneric.event.actions.item;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.PlaceEventComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.PlaceableComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemPlace implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockPlaceEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.hasComponent(PlaceEventComponent.class)) {
            PlaceEventComponent placeEvent = item.getComponent(PlaceEventComponent.class);
            placeEvent.handlePlace(event, player, item);
            return;
        }

        if (!item.hasComponent(PlaceableComponent.class)) {
            event.setCancelled(true);
        } else {
            PlaceableComponent placeableItem = item.getComponent(PlaceableComponent.class);
            if (placeableItem.getBlockType() != null) {
                event.setBlock(new SkyBlockBlock(placeableItem.getBlockType()).toBlock());
            }
        }
    }
}
