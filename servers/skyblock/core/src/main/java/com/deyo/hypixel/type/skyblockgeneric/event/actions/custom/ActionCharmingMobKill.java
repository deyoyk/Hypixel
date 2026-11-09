package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.utility.CooldownTracker;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeEffectService;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeId;
import com.deyo.hypixel.type.skyblockgeneric.hunting.CharmingService;

import java.util.UUID;

public class ActionCharmingMobKill implements HypixelEventClass {
    private static final CooldownTracker HEAL_COOLDOWN = new CooldownTracker();

    public static void clearCooldown(UUID playerId) {
        HEAL_COOLDOWN.clear(playerId);
    }

    @PhasedEvent(node = EventNodes.CUSTOM, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {
        CharmingService.tryCharm(event.getPlayer(), event.getKilledMob());
        double coins = AttributeEffectService.value(event.getPlayer().getHuntingData(), AttributeId.parse("C27"));
        if (coins > 0) event.getPlayer().addCoins(coins);
        double healing = AttributeEffectService.value(event.getPlayer().getHuntingData(), AttributeId.parse("U27"));
        if (healing > 0 && HEAL_COOLDOWN.tryUse(event.getPlayer().getUuid(), 500L)) {
            event.getPlayer().setHealth((float) Math.min(event.getPlayer().getMaxHealth(),
                    event.getPlayer().getHealth() + event.getPlayer().getMaxHealth() * healing / 100D));
        }
    }
}
