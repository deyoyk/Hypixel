package com.deyo.hypixel.type.replayviewer;

import com.deyo.hypixel.type.game.replay.ReplayMetadata;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.tab.TablistSkinRegistry;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class ReplayTablistModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer player) {
        List<TablistEntry> entries = new ArrayList<>();

        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> entries.addAll(entriesFor(session.getMetadata())),
                () -> entries.add(new TablistEntry("<7>Loading...", TablistSkinRegistry.ORANGE))
        );

        return entries;
    }

    static List<TablistEntry> entriesFor(ReplayMetadata metadata) {
        List<TablistEntry> entries = new ArrayList<>();
        for (Map.Entry<UUID, String> player : metadata.getPlayers().entrySet()) {
            String color = "<7>";
            Map<String, ReplayMetadata.TeamInfo> teamInfo = metadata.getTeamInfo();
            for (Map.Entry<String, List<UUID>> team : metadata.getTeams().entrySet()) {
                if (team.getValue().contains(player.getKey())) {
                    ReplayMetadata.TeamInfo info = teamInfo != null ? teamInfo.get(team.getKey()) : null;
                    if (info != null && info.colorCode() != null) {
                        color = info.colorCode();
                    }
                    break;
                }
            }
            entries.add(new TablistEntry(color + player.getValue(), TablistSkinRegistry.GRAY));
        }
        return entries;
    }
}