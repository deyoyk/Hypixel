package com.deyo.hypixel.type.generic.resourcepack;

import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.config.Settings;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public interface HypixelResourcePack {
    String getPackName();
    String getPackUrl();
    String getPackHash();

    boolean isRequired();

    void initialize();
    void onPlayerJoin(HypixelPlayer player);
    void onPlayerQuit(HypixelPlayer player);

    static Settings.ResourcePackSettings getConfigFor(String packName) {
        return ConfigProvider.settings().getResourcePacks()
                .getOrDefault(packName, new Settings.ResourcePackSettings());
    }
}
