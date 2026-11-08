package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gems.Gemstone;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

import java.util.List;

@Getter
public class GemstoneComponent extends SkyBlockItemComponent {
    private final List<GemstoneSlot> slots;

    public GemstoneComponent(List<GemstoneSlot> slots) {
        this.slots = slots;
    }

    public record GemstoneSlot(
            Gemstone.Slots slot,
            int unlockPrice,
            List<ItemRequirement> itemRequirements
    ) {}

    public record ItemRequirement(
            ItemType itemId,
            int amount
    ) {}
}