package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.gems.GemRarity;
import com.deyo.hypixel.type.skyblockgeneric.gems.Gemstone;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;

public class GemstoneImplComponent extends SkyBlockItemComponent {
    @Getter
    private final GemRarity gemRarity;
    @Getter
    private final Gemstone gemstone;

    public GemstoneImplComponent(GemRarity gemRarity, Gemstone gemstone, String skullTexture) {
        this.gemRarity = gemRarity;
        this.gemstone = gemstone;
        addInheritedComponent(new SkullHeadComponent((item) -> {
            return skullTexture;
        }));
    }

    public String getName() {
        return this.getClass().getSimpleName().split("(?=\\p{Upper})")[1];
    }
}
