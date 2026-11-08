package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.centauri;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributeGemData;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributeHotPotatoBookData;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.gems.Gemstone;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.EnchantableComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.GemstoneComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.HotPotatoableComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public final class CentauriItemMaximizer {
    private CentauriItemMaximizer() {}

    public static int maximize(SkyBlockPlayer player, SkyBlockItem item) {
        int applied = 0;
        applied += maximizeEnchantments(player, item);
        applied += maximizePotatoBooks(item);
        applied += maximizeGemstones(item);

        ItemAttributeHandler handler = item.getAttributeHandler();
        if (handler.getRarity().isCanReforge() && !handler.isRecombobulated()) {
            handler.setRecombobulated(true);
            applied++;
        }
        return applied;
    }

    private static int maximizeEnchantments(SkyBlockPlayer player, SkyBlockItem item) {
        if (!item.hasComponent(EnchantableComponent.class)) return 0;
        var itemGroups = item.getComponent(EnchantableComponent.class).getEnchantItemGroups();
        int applied = 0;

        for (EnchantmentType type : EnchantmentType.values()) {
            if (type.getEnch().getGroups().stream().noneMatch(itemGroups::contains)) continue;
            int maximumLevel = type.getEnch().getLevelsToApply(player).maximumLevel();
            var current = item.getAttributeHandler().getEnchantment(type);
            if (current != null && current.level() >= maximumLevel) continue;

            item.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(type, maximumLevel));
            applied++;
        }
        return applied;
    }

    private static int maximizePotatoBooks(SkyBlockItem item) {
        if (!item.hasComponent(HotPotatoableComponent.class)) return 0;
        HotPotatoableComponent component = item.getComponent(HotPotatoableComponent.class);
        ItemAttributeHotPotatoBookData.HotPotatoBookData data = item.getAttributeHandler().getHotPotatoBookData();
        int applied = 0;

        for (var entry : component.getApplicableItems().entrySet()) {
            int missing = entry.getValue() - data.getAmount(entry.getKey());
            if (missing <= 0) continue;
            data.addAmount(entry.getKey(), missing);
            applied += missing;
        }
        data.setPotatoType(component.getPotatoType());
        item.getAttributeHandler().setHotPotatoBookData(data);
        return applied;
    }

    private static int maximizeGemstones(SkyBlockItem item) {
        if (!item.hasComponent(GemstoneComponent.class)) return 0;
        GemstoneComponent component = item.getComponent(GemstoneComponent.class);
        ItemAttributeGemData.GemData data = item.getAttributeHandler().getGemData();
        int applied = 0;

        for (int index = 0; index < component.getSlots().size(); index++) {
            Gemstone gemstone = component.getSlots().get(index).slot().getValidGemstones().getFirst();
            ItemType perfectGem = gemstone.getItem().getLast();
            ItemAttributeGemData.GemData.GemSlots current = data.getGem(index);
            if (current == null) {
                data.putGem(new ItemAttributeGemData.GemData.GemSlots(index, perfectGem, true));
                applied++;
            } else {
                if (!current.isUnlocked()) { current.setUnlocked(true); applied++; }
                if (current.getFilledWith() != perfectGem) { current.setFilledWith(perfectGem); applied++; }
            }
        }
        item.getAttributeHandler().setGemData(data);
        return applied;
    }
}
