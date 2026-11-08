package com.deyo.hypixel.type.generic.tab;

import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.List;

public class EmptyTabModule extends TablistModule {

    public EmptyTabModule() {}

    public List<TablistEntry> getEntries(HypixelPlayer player) {
        return List.of();
    }
}
