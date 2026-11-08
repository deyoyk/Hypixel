package com.deyo.hypixel.type.generic.gui.v2.event;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.gui.v2.ViewNavigator;
import com.deyo.hypixel.type.generic.gui.v2.ViewSession;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionInventoryClose implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.INVENTORY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onPlayerInventoryClose(InventoryCloseEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ViewNavigator.find(player).ifPresent(navigator -> {
            ViewSession<?> session = navigator.getCurrentSession();
            if (session == null) {
                return;
            }
            if (event.getInventory() != session.inventory() || session.isSuppressCloseEvent()) {
                session.setSuppressCloseEvent(false);
                return;
            }
            session.close(ViewSession.CloseReason.PLAYER_EXITED);
        });
    }

}
