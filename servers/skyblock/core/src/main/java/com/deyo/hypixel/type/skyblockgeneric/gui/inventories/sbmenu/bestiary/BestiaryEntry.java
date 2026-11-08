package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary;

import com.deyo.hypixel.type.generic.gui.inventory.item.GUIMaterial;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;

import java.util.List;

public interface BestiaryEntry {
    String getName();
    String getDescription();
    GUIMaterial getGuiMaterial();
    List<BestiaryMob> getMobs();
}

