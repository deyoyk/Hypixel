package com.deyo.hypixel.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointBestiary;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.abstr.KillEventEnchant;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import org.jetbrains.annotations.NotNull;

public class PlayerKilledSkyBlockMobEvent implements PlayerInstanceEvent {
    private final SkyBlockPlayer player;
    @Getter
    private final SkyBlockMob killedMob;

    public PlayerKilledSkyBlockMobEvent(SkyBlockPlayer player, SkyBlockMob killedMob) {
        this.player = player;
        this.killedMob = killedMob;

        if (killedMob instanceof BestiaryMob bestiaryMob) {
            DatapointBestiary.PlayerBestiary playerBestiary = player.getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BESTIARY, DatapointBestiary.class).getValue();
            playerBestiary.increase(bestiaryMob, 1);
        }

        SkyBlockItem mainHandItem = PlayerItemOrigin.getFromCache(player.getUuid()).get(PlayerItemOrigin.MAIN_HAND);

        for (SkyBlockEnchantment enchantment : mainHandItem.getAttributeHandler().getEnchantments().toList()) {
            if (enchantment.type().getEnch() instanceof KillEventEnchant killEventEnchant) {
                killEventEnchant.onMobKilled(player, killedMob, enchantment.level());
            }
        }

        // Handle kill event pets
        SkyBlockItem pet = player.getPetData().getEnabledPet();
        player.getPetData().dispatch(new PetEvent.Kill(player, pet, killedMob));
    }

    @Override
    public @NotNull SkyBlockPlayer getPlayer() {
        return player;
    }
}
