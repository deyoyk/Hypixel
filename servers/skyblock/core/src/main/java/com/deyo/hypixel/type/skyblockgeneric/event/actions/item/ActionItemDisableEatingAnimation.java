package com.deyo.hypixel.type.skyblockgeneric.event.actions.item;

import net.minestom.server.event.item.PlayerBeginItemUseEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.DisableAnimationComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionItemDisableEatingAnimation implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBeginItemUseEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        SkyBlockItem item = new SkyBlockItem(player.getItemInMainHand());

        if (!(item.hasComponent(DisableAnimationComponent.class))) return;
        DisableAnimationComponent disableAnimationImpl = item.getComponent(DisableAnimationComponent.class);

        if (disableAnimationImpl.getDisabledAnimations().isEmpty()) return;
        if (!disableAnimationImpl.getDisabledAnimations().contains(event.getAnimation())) return;

        event.setCancelled(true);
    }
}
