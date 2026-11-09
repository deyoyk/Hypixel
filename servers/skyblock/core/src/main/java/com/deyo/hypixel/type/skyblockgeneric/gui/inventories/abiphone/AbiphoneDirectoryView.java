package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.abiphone;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.PaginatedView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.abiphone.AbiphoneNPC;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;

import java.util.List;

public final class AbiphoneDirectoryView extends PaginatedView<AbiphoneNPC, AbiphoneDirectoryView.State> {

    private static final int[] PAGINATED_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withText(
                (state, ctx) -> Text.key("gui_abiphone.contacts_directory"),
                InventoryType.CHEST_6_ROW
        );
    }

    @Override
    protected int[] getPaginatedSlots() {
        return PAGINATED_SLOTS;
    }

    @Override
    protected ItemStack.Builder renderItem(AbiphoneNPC npc, int index, HypixelPlayer player) {
        return ItemStacks.lore(
                ItemStacks.name(npc.getIcon(), "<f>{}", npc.getName()),
                List.of(Text.of("<7>{}", npc.getDescription()))
        );
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, AbiphoneNPC npc, int index) {
        ctx.player().sendMessage(Text.of("<7>{}", npc.getDescription()));
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, AbiphoneNPC item) {
        return !item.getName().plain().toLowerCase().contains(state.query().toLowerCase());
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.close(layout, 49);
        if (!Components.back(layout, 48, ctx)) {
            layout.slot(48, (s, c) -> Components.BACK_BUTTON, (click, viewCtx) -> {
                viewCtx.push(new AbiphoneView(), new AbiphoneView.State(click.state().abiphone()));
            });
        }
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    public record State(
            SkyBlockItem abiphone,
            List<AbiphoneNPC> items,
            int page,
            String query
    ) implements PaginatedState<AbiphoneNPC> {

        @Override
        public PaginatedState<AbiphoneNPC> withPage(int page) {
            return new State(abiphone, items, page, query);
        }

        @Override
        public PaginatedState<AbiphoneNPC> withItems(List<AbiphoneNPC> items) {
            return new State(abiphone, items, page, query);
        }
    }
}
