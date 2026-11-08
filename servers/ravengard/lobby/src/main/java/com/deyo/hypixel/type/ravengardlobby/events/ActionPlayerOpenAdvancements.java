package com.deyo.hypixel.type.ravengardlobby.events;

import net.minestom.server.advancements.AdvancementAction;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.network.packet.client.play.ClientAdvancementTabPacket;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.ravengardgeneric.gui.GUIRavengardMenu;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;

public class ActionPlayerOpenAdvancements implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, phase = EventPhase.GAMEPLAY)
    public void run(PlayerPacketEvent event) {
        if (event.getPacket() instanceof ClientAdvancementTabPacket packet) {
            if (packet.action() == AdvancementAction.OPENED_TAB) {
                if (event.getPlayer() instanceof RavengardPlayer player) {
                    player.openView(new GUIRavengardMenu());
                }
            }
        }
    }
}
