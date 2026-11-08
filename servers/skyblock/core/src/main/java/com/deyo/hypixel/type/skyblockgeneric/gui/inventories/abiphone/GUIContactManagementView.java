package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.abiphone;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.View;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.abiphone.AbiphoneNPC;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;


public final class GUIContactManagementView implements View<GUIContactManagementView.State> {

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.translatable("gui_abiphone.management.title", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);
        layout.slot(4, (s, c) -> ItemStacks.lore(
                ItemStacks.name(s.npc().getIcon(), "<f>{}", s.npc().getName()),
                "<7>{}", s.npc().getDescription()
        ));

        layout.slot(31, (s, c) -> ItemStacks.item(
                Material.FEATHER,
                1,
                Text.key("gui_abiphone.management.remove_contact"),
                Text.keyLines("gui_abiphone.management.remove_contact.lore")
        ), (click, viewCtx) -> {
            state.abiphone().getAttributeHandler().removeAbiphoneNPC(state.npc());
            viewCtx.player().sendMessage(Text.key("gui_abiphone.management.removed_message", state.npc().getName()));

            if (!viewCtx.pop()) {
                viewCtx.player().closeInventory();
            }
        });

        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, (s, c) -> Components.BACK_BUTTON, (click, viewCtx) -> {
                viewCtx.push(new AbiphoneView(), new AbiphoneView.State(state.abiphone()));
            });
        }

        Components.close(layout, 49);
    }

    public record State(SkyBlockItem abiphone, AbiphoneNPC npc) {}
}

