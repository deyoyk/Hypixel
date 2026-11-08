package com.deyo.hypixel.type.lobby.events;

import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.item.LobbyItem;
import com.deyo.hypixel.type.lobby.visibility.PlayerVisibilityManager;

import java.util.Map;

public class LobbyPlayerSpawnEvents implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader lobbyLoader) {
            Map<Integer, LobbyItem> hotbarItems = lobbyLoader.getHotbarItems();
            for (Map.Entry<Integer, LobbyItem> entry : hotbarItems.entrySet()) {
                player.getInventory().setItemStack(
                    entry.getKey(),
                    entry.getValue().getItemStack(player)
                );
            }
        }

        PlayerVisibilityManager.setupViewerRuleFromToggle(player);
        player.setGameMode(GameMode.SURVIVAL);
    }
}