package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.loot;

import com.deyo.hypixel.type.skyblockgeneric.bestiary.BestiaryData;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.impl.EnchantmentScavenger;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class CoinDropHandler implements HypixelEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockMob mob = event.getKilledMob();

        if (mob == null) return;

        int coinsDropped;

        //Base Mob Coins (additive)
        int baseCoins = mob.getOtherLoot().getCoinAmount();

        //Scavenger Enchant (additive)
        EnchantmentScavenger enchantmentScavenger = new EnchantmentScavenger();
        int scavengedCoins = enchantmentScavenger.getScavengedCoins(event);

        //Bestiary Boost (multiplicative)
        float bestiaryBoost = 1;
        if (mob instanceof BestiaryMob bestiaryMob) {
            int tier = bestiaryData.getCurrentBestiaryTier(bestiaryMob, player.getBestiaryData().getAmount(bestiaryMob));
            bestiaryBoost += (float) bestiaryData.getTotalExtraCoinPercentage(tier) / 100;
        }

        coinsDropped = Math.round((baseCoins + scavengedCoins) * bestiaryBoost);

        player.addCoins(coinsDropped);
    }
}
