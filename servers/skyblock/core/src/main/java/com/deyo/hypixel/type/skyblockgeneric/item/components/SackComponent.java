package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bags.GUISack;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class SackComponent extends SkyBlockItemComponent {
    @Getter
    private final List<ItemType> validItems;
    @Getter
    private final int maxCapacity;

    public SackComponent(List<String> items, int maxCapacity) {
        this.validItems = items.stream()
                .map(ItemType::valueOf)
                .toList();
        this.maxCapacity = maxCapacity;
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new InteractableComponent(this::onInteract, null, null));
    }

    private void onInteract(SkyBlockPlayer player, SkyBlockItem item) {
        player.openView(new GUISack(item.getItemType(), false));
    }
}
