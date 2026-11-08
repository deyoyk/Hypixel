package com.deyo.hypixel.packer.packs.ravengard;

import com.deyo.hypixel.packer.PackDefinition;

public class RavengardPackDefinition implements PackDefinition {
    public static final RavengardPackDefinition INSTANCE = new RavengardPackDefinition();

    @Override
    public String getPackName() {
        return "ravengard";
    }

    @Override
    public String getPackDirectory() {
        return "configuration/resourcepacks/ravengard";
    }
}
