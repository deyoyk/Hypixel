package com.deyo.hypixel.type.hub.gui.rosetta;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;

import java.util.Map;

public class GUIRosettaIronArmor extends ShopView {
    public GUIRosettaIronArmor() {
        super("Iron Armor", DEFAULT);
    }

    private SkyBlockItem applyEnchantment (SkyBlockItem item, Map<EnchantmentType, Integer> enchantments) {
        ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
        enchantments.forEach(((enchantmentType, level) ->
                itemAttributeHandler.addEnchantment(new SkyBlockEnchantment(enchantmentType, level))));
        return itemAttributeHandler.asSkyBlockItem();
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_HELMET), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(15)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_CHESTPLATE), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(25)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_LEGGINGS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(30)));
        attachItem(ShopItem.Single(applyEnchantment(new SkyBlockItem(ItemType.IRON_BOOTS), Map.of(EnchantmentType.GROWTH, 1)), 1, new CoinShopPrice(20)));
    }
}
