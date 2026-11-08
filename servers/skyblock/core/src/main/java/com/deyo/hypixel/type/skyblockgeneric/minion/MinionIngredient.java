package com.deyo.hypixel.type.skyblockgeneric.minion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.deyo.hypixel.commons.skyblock.item.ItemType;

@AllArgsConstructor
@Getter
public class MinionIngredient {
    private ItemType item;
    private Integer amount;
}
