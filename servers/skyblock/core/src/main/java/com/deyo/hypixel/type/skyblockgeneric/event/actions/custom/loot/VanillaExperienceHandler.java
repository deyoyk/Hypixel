package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.loot;

import com.deyo.hypixel.type.skyblockgeneric.bestiary.BestiaryData;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.impl.EnchantmentExperience;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerKilledSkyBlockMobEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class VanillaExperienceHandler implements HypixelEventClass {
    BestiaryData bestiaryData = new BestiaryData();

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerKilledSkyBlockMobEvent event) {

        SkyBlockPlayer player = event.getPlayer();
        SkyBlockMob mob = event.getKilledMob();

        if (mob == null) return;

        long xpOrbsDropped;

        // Base XP Orbs (additive)
        long baseXpOrbs = mob.getOtherLoot().getXpOrbAmount();

        // Bestiary Boost (multiplicative)
        float bestiaryBoost = 1;
        if (mob instanceof BestiaryMob bestiaryMob) {
            int tier = bestiaryData.getCurrentBestiaryTier(bestiaryMob, player.getBestiaryData().getAmount(bestiaryMob));
            bestiaryBoost += (float) bestiaryData.getTotalExtraXPPercentage(tier) / 100;
        }

        // Experience enchantment (multiplicative)
        float experienceEnchBoost = 1;
        SkyBlockItem mainHandItem = PlayerItemOrigin.getFromCache(player.getUuid()).get(PlayerItemOrigin.MAIN_HAND);
        SkyBlockEnchantment experienceEnchantment = mainHandItem.getAttributeHandler().getEnchantment(EnchantmentType.EXPERIENCE);
        if (experienceEnchantment != null) {
            if (EnchantmentExperience.shouldDoubleExperience(experienceEnchantment.level())) {
                experienceEnchBoost *= 2;
            }
        }

        xpOrbsDropped = Math.round((baseXpOrbs) * bestiaryBoost * experienceEnchBoost);

        player.addExperience(xpOrbsDropped);
    }
}
