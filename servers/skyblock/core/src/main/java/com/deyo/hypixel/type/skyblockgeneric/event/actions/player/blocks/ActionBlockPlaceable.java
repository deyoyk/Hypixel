package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.blocks;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.block.SkyBlockBlock;
import com.deyo.hypixel.type.skyblockgeneric.block.impl.BlockPlaceable;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.PlaceableComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionBlockPlaceable implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onPlace(PlayerBlockPlaceEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInMainHand();
        SkyBlockItem item = new SkyBlockItem(itemStack);
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (item.toConfigurableItem() == null) return;

        if (item.hasComponent(PlaceableComponent.class)) {
            PlaceableComponent placeable = item.getComponent(PlaceableComponent.class);
            if (placeable.getBlockType() == null) return;
            SkyBlockBlock skyBlockBlock = new SkyBlockBlock(placeable.getBlockType());
            if (skyBlockBlock.getGenericInstance() instanceof BlockPlaceable blockPlaceable) {
                blockPlaceable.onPlace(event, skyBlockBlock);
            }
        }
    }
}
