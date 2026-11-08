package com.deyo.hypixel.type.lobby.events;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PlayerFinishItemUseEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.item.LobbyItemHandler;

public class LobbyItemEvents implements HypixelEventClass {

    private static LobbyItemHandler getHandler() {
        if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
            return lobbyLoader.getItemHandler();
        }
        return null;
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onItemFinishUse(PlayerFinishItemUseEvent event) {
        LobbyItemHandler handler = getHandler();
        if (handler != null) {
            handler.onItemFinishUse(event);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onItemUseOnBlock(PlayerUseItemOnBlockEvent event) {
        LobbyItemHandler handler = getHandler();
        if (handler != null) {
            handler.onItemUseOnBlock(event);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onItemUse(PlayerUseItemEvent event) {
        LobbyItemHandler handler = getHandler();
        if (handler != null) {
            handler.onItemUse(event);
        }
    }

    @PhasedEvent(node = EventNodes.ITEM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onItemDrop(ItemDropEvent event) {
        LobbyItemHandler handler = getHandler();
        if (handler != null) {
            handler.onItemDrop(event);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        LobbyItemHandler handler = getHandler();
        if (handler != null) {
            handler.onBlockPlace(event);
        }
    }
}
