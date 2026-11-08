package com.deyo.hypixel.type.bedwarsgame.shop;

import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;

public class InteractableShopItem extends ShopItem {
    private final SimpleInteractableItem item;

    public InteractableShopItem(SimpleInteractableItem item) {
        super(
            item.getId(),
            item.getShopData().name(),
            item.getShopData().description(),
            item.getShopData().price(),
            item.getShopData().amount(),
            item.getShopData().currency(),
            item.getBlandItem().material(),
            item.getShopData().hotbarItemType()
        );
        this.item = item;
    }

    @Override
    public void onPurchase(BedWarsPlayer player) {
        giveItem(player, item.getItemStack().withAmount(getAmount()));
    }
}
