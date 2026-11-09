package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUISkyBlockSettings extends StatelessView {
    private static final List<Setting> SETTINGS = List.of(
            new Setting(11, DatapointToggles.Toggles.ToggleType.PURCHASE_CONFIRMATION_BITS,
                    "<6>Purchase Confirmation",
                    List.of("<7>Ask for confirmation before", "<7>purchasing items with Bits.")),
            new Setting(12, DatapointToggles.Toggles.ToggleType.SKYBLOCK_LEVELS_IN_CHAT,
                    "<b>SkyBlock Levels in Chat",
                    List.of("<7>Show SkyBlock level prefixes", "<7>next to player names in chat.")),
            new Setting(13, DatapointToggles.Toggles.ToggleType.DISABLE_DROP_MESSAGES,
                    "<e>Hide Drop Messages",
                    List.of("<7>Hide the message shown when", "<7>dropping an item.")),
            new Setting(14, DatapointToggles.Toggles.ToggleType.PAPER_ICONS,
                    "<a>Fast Travel Icons",
                    List.of("<7>Use paper icons for travel", "<7>scrolls in fast travel menus."))
    );

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Settings", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 30, ctx);
        Components.close(layout, 31);

        for (Setting setting : SETTINGS) {
            layout.slot(setting.slot(), (s, c) -> {
                SkyBlockPlayer player = (SkyBlockPlayer) c.player();
                boolean enabled = player.getToggles().get(setting.toggle());
                List<Text> lore = new ArrayList<>();
                lore.add(Text.of(enabled ? "<a>Enabled" : "<c>Disabled"));
                lore.add(Text.literal(" "));
                for (String line : setting.lore()) {
                    lore.add(Text.of(line));
                }
                lore.add(Text.literal(" "));
                lore.add(Text.of("<e>Click to toggle!"));
                return ItemStacks.item(enabled ? Material.LIME_DYE : Material.GRAY_DYE, 1,
                        Text.of(setting.name()), lore);
            }, (click, c) -> {
                toggle((SkyBlockPlayer) c.player(), setting);
                c.session(DefaultState.class).render();
            });
        }
    }

    static boolean toggle(SkyBlockPlayer player, Setting setting) {
        return player.getToggles().inverse(setting.toggle());
    }

    static List<Setting> settings() {
        return SETTINGS;
    }

    record Setting(int slot, DatapointToggles.Toggles.ToggleType toggle, String name, List<String> lore) {
    }
}
