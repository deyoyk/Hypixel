package com.deyo.hypixel.type.skyblockgeneric.event.actions.entity;

import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.network.packet.server.play.DamageEventPacket;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionAnimateEntityDamage implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(EntityDamageEvent event) {
        event.setAnimation(true);
        event.setCancelled(false);

        event.getEntity().sendPacketToViewersAndSelf(new DamageEventPacket(event.getEntity().getEntityId(),
                        0,
                        0,
                        0,
                        null));
    }
}
