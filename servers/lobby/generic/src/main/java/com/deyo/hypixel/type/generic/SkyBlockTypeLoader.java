package com.deyo.hypixel.type.generic;

import com.deyo.hypixel.commons.Songs;

public interface SkyBlockTypeLoader extends HypixelTypeLoader {
    default Songs getIslandSong() {
        return null;
    }
}
