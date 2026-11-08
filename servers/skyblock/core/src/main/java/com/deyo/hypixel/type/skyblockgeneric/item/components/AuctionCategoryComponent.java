package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionCategories;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class AuctionCategoryComponent extends SkyBlockItemComponent {
    @Getter
    private final AuctionCategories category;

    public AuctionCategoryComponent(String category) {
        this.category = AuctionCategories.valueOf(category);
    }
}