package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;

public class SkillableMineComponent extends SkyBlockItemComponent {
    @Getter
    private final SkillCategories category;
    @Getter
    private final double miningValue;

    public SkillableMineComponent(String category, double miningValue) {
        this.category = SkillCategories.valueOf(category);
        this.miningValue = miningValue;
    }
}