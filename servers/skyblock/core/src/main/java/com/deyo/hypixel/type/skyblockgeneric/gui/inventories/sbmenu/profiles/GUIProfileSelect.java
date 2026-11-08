package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.SkyBlockPlayerProfiles;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointString;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.generic.data.mongodb.UserDatabase;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.*;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.data.ProfileSwitcher;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public class GUIProfileSelect extends StatelessView {
    private final UUID profileUuid;

    public GUIProfileSelect(UUID profileUuid) {
        this.profileUuid = profileUuid;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.select.title", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        // Switch to Profile
        layout.slot(11, (s, c) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) c.player();
            String currentProfile = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            String switchingTo;
            try {
                switchingTo = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument())
                        .get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue();
            } catch (Exception e) {
                switchingTo = "Unknown";
            }

            return ItemStacks.item(Material.GRASS_BLOCK, 1, Text.key("gui_sbmenu.profiles.select.switch"),
                    Text.keyLines("gui_sbmenu.profiles.select.switch.lore", currentProfile, switchingTo));
        }, (click, c) -> ProfileSwitcher.switchTo((SkyBlockPlayer) c.player(), profileUuid));

        // Delete Profile
        layout.slot(15, (s, c) -> ItemStacks.item(Material.RED_STAINED_GLASS, 1, Text.key("gui_sbmenu.profiles.select.delete"),
                        Text.keyLines("gui_sbmenu.profiles.select.delete.lore")),
                (click, c) -> {
                    SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                    if (CoopDatabase.getFromMemberProfile(profileUuid) != null) {
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.cannot_delete_coop"));
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.coop_leave"));
                        return;
                    }

                    SkyBlockPlayerProfiles profiles = player.getProfiles();
                    profiles.removeProfile(profileUuid);
                    new UserDatabase(player.getUuid()).saveProfiles(profiles);

                    try {
                        SkyBlockDataHandler handler = SkyBlockDataHandler.createFromProfileOnly(new ProfilesDatabase(profileUuid.toString()).getDocument());
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.deleted",
                                handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue()));
                    } catch (Exception e) {
                        player.sendMessage(Text.key("gui_sbmenu.profiles.select.msg.deleted_generic"));
                    }

                    ProfilesDatabase.deleteDocument(profileUuid.toString());
                    player.openView(new GUIProfileManagement());
                });
    }
}
