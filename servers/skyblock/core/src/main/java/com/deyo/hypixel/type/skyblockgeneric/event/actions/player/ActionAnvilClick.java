package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUIAnvil;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionAnvilClick implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (!event.getBlock().name().equals("minecraft:anvil")) return;

        new GUIAnvil().open(player);
    }
}
