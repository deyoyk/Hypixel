package com.deyo.hypixel.type.generic.gui.v2.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.v2.ViewNavigator;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionInventoryOpen implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.INVENTORY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onPlayerInventoryOpen(InventoryOpenEvent event) {
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            HypixelPlayer player = (HypixelPlayer) event.getPlayer();
            ViewNavigator.find(player).ifPresent(navigator -> {
                if (navigator.getCurrentSession() == null) {
                    // could also be that this is a legacy view. However, why is ViewNavigator lingering then?
                    Logger.warn("Current session is null for player {} when opening inventory, this should not happen.", player.getUsername());
                    return;
                }
                navigator.getCurrentSession().onOpenEvent(event);
            });
        });

    }

}
