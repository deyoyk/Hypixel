package com.deyo.hypixel.type.island.tab;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.tab.TablistSkinRegistry;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IslandGuestsModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        Locale l = player.getLocale();
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                centered(Text.key("tablist.module.guests"), l, TablistSkinRegistry.PURPLE)
        ));

        fillRestWithGray(entries);

        return entries;
    }
}
