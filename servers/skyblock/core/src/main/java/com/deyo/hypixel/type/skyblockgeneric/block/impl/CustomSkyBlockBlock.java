package com.deyo.hypixel.type.skyblockgeneric.block.impl;

import lombok.NonNull;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public interface CustomSkyBlockBlock {
    @NonNull
    Block getDisplayMaterial();

    @NonNull
    Boolean shouldPlace(SkyBlockPlayer player);

    @NonNull
    Boolean shouldDestroy(SkyBlockPlayer player);
}
