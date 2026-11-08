package com.deyo.hypixel.type.hub.gui.elizabeth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.ItemType;

@AllArgsConstructor
@Getter
public class CommunityShopItem {
    private ItemType itemType;
    private int price;
    private int amount;
}
