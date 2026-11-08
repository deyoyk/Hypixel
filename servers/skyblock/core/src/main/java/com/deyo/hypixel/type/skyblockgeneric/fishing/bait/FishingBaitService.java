package com.deyo.hypixel.type.skyblockgeneric.fishing.bait;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.fishing.FishingMedium;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingBaitComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingBaitService {

    public static @Nullable FishingBaitComponent getFirstAvailableBait(SkyBlockPlayer player, FishingMedium medium) {
        for (int slot = 0; slot < 36; slot++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(slot));
            ItemType type = item.getAttributeHandler().getPotentialType();
            if (type == null) {
                continue;
            }

            FishingBaitComponent baitComponent = item.getComponent(FishingBaitComponent.class);
            if (baitComponent == null) {
                continue;
            }
            if (!baitComponent.getMediums().isEmpty() && !baitComponent.getMediums().contains(medium)) {
                continue;
            }
            return baitComponent;
        }
        return null;
    }

    public static boolean consumeOneBait(SkyBlockPlayer player, String baitItemId) {
        for (int slot = 0; slot < 36; slot++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(slot));
            if (item.getAttributeHandler().getPotentialType() == null) {
                continue;
            }
            if (!item.getAttributeHandler().getPotentialType().name().equals(baitItemId)) {
                continue;
            }

            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
                player.getInventory().setItemStack(slot, PlayerItemUpdater.playerUpdate(player, item.getItemStack()).build());
            } else {
                player.getInventory().setItemStack(slot, ItemStack.AIR);
            }
            return true;
        }
        return false;
    }
}
