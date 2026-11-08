package com.deyo.hypixel.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

public class TrackerShopView extends StatelessView {

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return ViewConfiguration.withString((_, _) -> "Tracker Shop", InventoryType.CHEST_6_ROW);
    }


    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.back(layout, 49, ctx);
    }

}
