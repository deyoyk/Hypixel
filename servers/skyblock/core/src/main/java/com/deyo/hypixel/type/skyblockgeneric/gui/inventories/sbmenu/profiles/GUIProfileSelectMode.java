package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

public class GUIProfileSelectMode extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.translatable("gui_sbmenu.profiles.mode.title", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        layout.slot(11, (s, c) -> ItemStacks.item(Material.GRASS_BLOCK, 1, Text.key("gui_sbmenu.profiles.mode.classic"),
                        Text.keyLines("gui_sbmenu.profiles.mode.classic.lore")),
                (click, c) -> c.player().openView(new GUIProfileCreate(com.deyo.hypixel.type.skyblockgeneric.user.ProfileMode.CLASSIC)));

        layout.slot(15, (s, c) -> ItemStacks.item(Material.BLAZE_POWDER, 1, Text.key("gui_sbmenu.profiles.mode.special"),
                        Text.keyLines("gui_sbmenu.profiles.mode.special.lore")),
            (click, c) -> c.player().openView(new GUIProfileSelectSpecialMode()));
    }
}
