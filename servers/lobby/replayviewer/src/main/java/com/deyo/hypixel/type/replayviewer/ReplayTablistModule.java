package com.deyo.hypixel.type.replayviewer;

import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.tab.TablistSkinRegistry;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;

class ReplayTablistModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        List<TablistEntry> entries = new ArrayList<>();

        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> {
                // TODO: display all player names with only their color
            },
                () -> entries.add(new TablistEntry("<7>Loading...", TablistSkinRegistry.ORANGE))
        );

        return entries;
    }
}