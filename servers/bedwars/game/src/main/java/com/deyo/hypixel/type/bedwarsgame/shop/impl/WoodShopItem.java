package com.deyo.hypixel.type.bedwarsgame.shop.impl;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.ShopItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.collectibles.bedwars.BedWarsWoodSkinRuntimeService;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBedWarsHotbar;

public class WoodShopItem extends ShopItem {

    public WoodShopItem() {
        super(
            "wood",
            "Wood",
            "Good block to defend your bed.\nStrong against pickaxes.",
            4,
            16,
            Currency.GOLD,
            Material.OAK_PLANKS,
            DatapointBedWarsHotbar.HotbarItemType.BLOCKS
        );
    }

    @Override
    public void onPurchase(BedWarsPlayer player) {
        Material selectedWood = BedWarsWoodSkinRuntimeService.resolveSelectedMaterial(player);
        giveItem(player, ItemStack.of(selectedWood).withAmount(getAmount()));
    }

    @Override
    public ItemStack getDisplay(BedWarsPlayer player) {
        Material selectedWood = BedWarsWoodSkinRuntimeService.resolveSelectedMaterial(player);
        return ItemStack.of(selectedWood);
    }
}
