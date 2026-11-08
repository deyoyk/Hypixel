package com.deyo.hypixel.commons.skyblock.item;

import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record UnderstandableSkyBlockItem(@Nullable ItemType itemKey, List<ItemAttribute> attributes,
                                         int amount, Material material) {
    public UnderstandableSkyBlockItem() {
        this(ItemType.AIR, List.of(), 1, Material.AIR);
    }

    public UnderstandableSkyBlockItem(int amount) {
        this(ItemType.AIR, List.of(), amount, Material.AIR);
    }

    public ItemAttribute getAttribute(String key) {
        for (ItemAttribute attribute : attributes) {
            if (attribute.getKey().equals(key)) {
                return attribute;
            }
        }
        return null;
    }

    public String serialize() {
        return new UnderstandableSkyBlockItemSerializer().serialize(this);
    }

    public static UnderstandableSkyBlockItem deserialize(String json) {
        return new UnderstandableSkyBlockItemSerializer().deserialize(json);
    }
}
