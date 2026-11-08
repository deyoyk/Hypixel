package com.deyo.hypixel.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.bedwarsgame.shop.impl.AxeShopItem;
import com.deyo.hypixel.type.bedwarsgame.shop.impl.PickaxeShopItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;

public class PermanentToolLuckyReward extends LuckyReward {
    private final boolean axe;

    public PermanentToolLuckyReward(String name, boolean axe) {
        super(name);
        this.axe = axe;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        if (axe) {
            new AxeShopItem().onPurchase(player);
        } else {
            new PickaxeShopItem().onPurchase(player);
        }
    }
}
