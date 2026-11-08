package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeEffectService;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeId;
import com.deyo.hypixel.type.skyblockgeneric.hunting.CharmingService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActionCharmingMobKill implements HypixelEventClass {
    private static final Map<UUID, Long> HEAL_COOLDOWN = new HashMap<>();

    @PhasedEvent(node = EventNodes.CUSTOM, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        CharmingService.tryCharm(event.getPlayer(), event.getKilledMob());
        double coins = AttributeEffectService.value(event.getPlayer().getHuntingData(), AttributeId.parse("C27"));
        if (coins > 0) event.getPlayer().addCoins(coins);
        long now = System.currentTimeMillis();
        if (now >= HEAL_COOLDOWN.getOrDefault(event.getPlayer().getUuid(), 0L)) {
            double healing = AttributeEffectService.value(event.getPlayer().getHuntingData(), AttributeId.parse("U27"));
            if (healing > 0) {
                event.getPlayer().setHealth((float) Math.min(event.getPlayer().getMaxHealth(),
                        event.getPlayer().getHealth() + event.getPlayer().getMaxHealth() * healing / 100D));
                HEAL_COOLDOWN.put(event.getPlayer().getUuid(), now + 500);
            }
        }
    }
}
