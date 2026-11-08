package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.profiles;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.user.ProfileMode;

public class GUIProfileSelectSpecialMode extends StatelessView {
    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Select a Special Mode", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        layout.slot(12, ItemStacks.item(Material.IRON_CHESTPLATE, 1, """
                        <7>♲ <7>Ironman
                        <8>Special Mode

                        <c>✖ No Auction House!
                        <c>✖ No Bazaar!
                        <c>✖ No /trade!

                        <a>✔ Can buy Booster Cookies!
                        <a>✔ Can buy cosmetics!

                        <b>This mode can be played in co-op!
                        <8>Co-op members may still trade together.

                        <e>Click to start new profile!"""),
                (click, c) -> c.player().openView(new GUIProfileCreate(ProfileMode.IRONMAN)));
        layout.slot(31, ItemStacks.item(Material.ARROW, 1, """
                        <a>Go Back
                        <7>To Choose a SkyBlock Mode"""),
                (click, c) -> c.player().openView(new GUIProfileSelectMode()));
    }
}
