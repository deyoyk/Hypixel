package com.deyo.hypixel.type.skyblockgeneric.item.crafting.grid;

import com.deyo.hypixel.type.generic.gui.v2.click.SlotStack;

public interface CraftingResolver {

    SlotStack result(SlotStack[] grid);

    SlotStack[] consume(SlotStack[] grid);
}
