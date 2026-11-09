package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.levels.emblem;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIMaterial;
import com.deyo.hypixel.type.generic.gui.v2.*;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockEmblems;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUIEmblems extends StatelessView {
    private static final int[] SLOTS = new int[]{11, 12, 13, 14, 15};
    private final SkyBlockPlayer target;

    public GUIEmblems() {
        this(null);
    }

    public GUIEmblems(SkyBlockPlayer target) {
        this.target = target;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Emblems", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 31);
        Components.back(layout, 30, ctx);

        for (SkyBlockEmblems emblem : SkyBlockEmblems.values()) {
            if (emblem.ordinal() >= SLOTS.length) break;
            int slot = SLOTS[emblem.ordinal()];

            layout.slot(slot, (s, c) -> {
                SkyBlockPlayer owner = owner(c.player(), target);
                String displayName = emblem.toString();
                GUIMaterial guiMaterial = emblem.getGuiMaterial();

                List<Text> lore = new ArrayList<>();
                lore.add(Text.of("<8>{} Unlocked", emblem.amountUnlocked(owner)));
                lore.add(Text.literal(" "));
                lore.addAll(emblem.getDescription());
                lore.add(Text.literal(" "));
                lore.add(Text.of("<e>Click to view!"));

                return ItemStacks.of(guiMaterial, 1, Text.of("<a>{}", displayName), lore);
            }, (click, c) -> c.push(new GUIEmblem(emblem, target), GUIEmblem.createInitialState(emblem)));
        }
    }

    static SkyBlockPlayer owner(HypixelPlayer viewer, SkyBlockPlayer target) {
        return target != null ? target : (SkyBlockPlayer) viewer;
    }
}
