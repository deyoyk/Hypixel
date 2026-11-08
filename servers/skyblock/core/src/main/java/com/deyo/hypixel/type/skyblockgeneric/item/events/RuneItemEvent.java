package com.deyo.hypixel.type.skyblockgeneric.item.events;

import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class RuneItemEvent implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        SkyBlockItem runedItem = player.getStatistics().getItemWithRune(ItemType.BLOOD_RUNE);
        if (runedItem == null) return;
        int level = runedItem.getAttributeHandler().getRuneData().getLevel();
        int amountOfParticles = level * 3;

        player.sendPacket(new ParticlePacket(Particle.DRIPPING_LAVA, true, false,
                event.getKilledMob().getPosition().x(), event.getKilledMob().getPosition().y(), event.getKilledMob().getPosition().z(),
                0, 0, 0, 0, amountOfParticles));
    }
}
