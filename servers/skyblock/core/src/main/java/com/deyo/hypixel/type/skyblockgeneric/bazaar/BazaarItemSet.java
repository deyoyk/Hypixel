package com.deyo.hypixel.type.skyblockgeneric.bazaar;

import com.deyo.hypixel.commons.skyblock.item.ItemType;

import java.util.List;

public class BazaarItemSet {
    public List<ItemType> items;
    public ItemType displayMaterial;
    public String displayName;

    public BazaarItemSet(ItemType displayMaterial, String displayName, ItemType... items) {
        this.displayMaterial = displayMaterial;
        this.displayName = displayName;
        this.items = List.of(items);
    }
}
