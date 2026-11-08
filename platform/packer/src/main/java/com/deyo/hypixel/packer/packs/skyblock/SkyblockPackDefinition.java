package com.deyo.hypixel.packer.packs.skyblock;

import com.deyo.hypixel.packer.PackDefinition;

public class SkyblockPackDefinition implements PackDefinition {
    public static final SkyblockPackDefinition INSTANCE = new SkyblockPackDefinition();

    @Override
    public String getPackName() {
        return "skyblockpack";
    }

    @Override
    public String getPackDirectory() {
        return "configuration/resourcepacks/skyblockpack";
    }
}
