package com.deyo.hypixel.type.island.tab;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.tab.TablistSkinRegistry;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandMembers;
import com.deyo.hypixel.type.skyblockgeneric.user.island.SkyBlockIsland;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IslandGuestsModule extends TablistModule {
    @Override
    public List<TablistEntry> getEntries(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        Locale l = player.getLocale();
        List<SkyBlockPlayer> guests = guests(player);
        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                centered(Text.key("tablist.module.guests", guests.size()), l, TablistSkinRegistry.PURPLE)
        ));

        for (int x = 0; x < 19; x++) {
            if (x >= guests.size()) {
                entries.add(getGrayEntry());
                continue;
            }
            entries.add(new TablistEntry(Text.of("{}", guests.get(x).getRankDisplayName()), TablistSkinRegistry.GRAY));
        }

        fillRestWithGray(entries);
        return entries;
    }

    public static List<SkyBlockPlayer> guests(SkyBlockPlayer viewer) {
        SkyBlockIsland island = viewer.getSkyBlockIsland();
        if (island == null || island.getIslandInstance() == null) {
            return List.of();
        }

        Map<UUID, SkyBlockPlayer> players = island.getIslandInstance().getPlayers().stream()
                .filter(SkyBlockPlayer.class::isInstance)
                .map(SkyBlockPlayer.class::cast)
                .collect(Collectors.toMap(SkyBlockPlayer::getUuid, Function.identity()));
        Set<UUID> members = IslandMembers.onlineMembers(island.getIslandID(), island.getCoop()).stream()
                .map(SkyBlockPlayer::getUuid)
                .collect(Collectors.toSet());

        return guestIds(players.keySet(), members).stream().map(players::get).toList();
    }

    static List<UUID> guestIds(Collection<UUID> islandPlayerIds, Collection<UUID> memberIds) {
        return islandPlayerIds.stream()
                .filter(id -> !memberIds.contains(id))
                .toList();
    }
}
