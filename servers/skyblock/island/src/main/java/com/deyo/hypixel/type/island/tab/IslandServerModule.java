package com.deyo.hypixel.type.island.tab;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.tab.TablistSkinRegistry;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IslandServerModule extends TablistModule {

    @Override
    public List<TablistModule.TablistEntry> getEntries(HypixelPlayer p) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        Locale l = player.getLocale();
        List<IslandMinionData.IslandMinion> minions = player.getSkyBlockIsland().getMinionData().getMinions();
        DatapointMinionData.ProfileMinionData data = player.getSkyblockDataHandler().get(
                SkyBlockDataHandler.Data.MINION_DATA,
                DatapointMinionData.class
        ).getValue();

        ArrayList<TablistEntry> entries = new ArrayList<>(List.of(
                centered(Text.key("tablist.module.server_info"), l, TablistSkinRegistry.CYAN)
        ));

        entries.add(new TablistEntry(Text.key("tablist.server_info.area.private_island"), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(Text.key("tablist.server_info.server_label", HypixelConst.getServerName()), TablistSkinRegistry.GRAY));
        entries.add(new TablistEntry(Text.key("tablist.island.minions_label", minions.size(), data.getSlots()), TablistSkinRegistry.GRAY));

        entries.add(getGrayEntry());
        entries.add(new TablistEntry(Text.key("tablist.module.minions", minions.size()), TablistSkinRegistry.GRAY));

        minions.forEach(minion -> {
            MinionHandler.InternalMinionTags.State minionState = minion.getInternalMinionTags().getState();
            String stateKey = switch (minionState) {
                case BAD_FULL -> "tablist.island.minion_state.full";
                case BAD_LOCATION -> "tablist.island.minion_state.blocked";
                default -> "tablist.island.minion_state.active";
            };

            Text rendered = Text.of(" {} {:roman} ",
                            minion.getMinion().getDisplay().replace(" Minion", ""), minion.getTier())
                    .append(Text.key(stateKey));
            entries.add(new TablistEntry(rendered, TablistSkinRegistry.GRAY));
        });

        fillRestWithGray(entries);

        return entries;
    }
}
