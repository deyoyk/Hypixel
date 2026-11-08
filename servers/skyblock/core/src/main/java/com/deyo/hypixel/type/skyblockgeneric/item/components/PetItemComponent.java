package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class PetItemComponent extends SkyBlockItemComponent {
    public PetItemComponent() {
        addInheritedComponent(new ExtraRarityComponent("PET ITEM"));
        addInheritedComponent(new ExtraUnderNameComponent("Consumed on use"));
        addInheritedComponent(new TrackedUniqueComponent());
    }
}