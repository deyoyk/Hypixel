package com.deyo.hypixel.type.replayviewer.view;

import net.minestom.server.entity.Entity;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.PaginatedView.PaginatedState;
import com.deyo.hypixel.type.generic.gui.v2.StatefulPaginatedView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.replayviewer.TypeReplayViewerLoader;
import com.deyo.hypixel.type.replayviewer.entity.ReplayPlayerEntity;
import com.deyo.hypixel.type.replayviewer.playback.ReplaySession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GUIPlayers extends StatefulPaginatedView<GUIPlayers.PlayerEntry, GUIPlayers.State> {

    record PlayerEntry(int entityId, ReplayPlayerEntity entity) {
    }

    public record State(List<PlayerEntry> items, int page) implements PaginatedState<PlayerEntry> {
        @Override
        public PaginatedState<PlayerEntry> withPage(int page) {
            return new State(items, page);
        }

        @Override
        public PaginatedState<PlayerEntry> withItems(List<PlayerEntry> items) {
            return new State(items, page);
        }
    }

    @Override
    public State initialState() {
        return new State(List.of(), 0);
    }

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Players", InventoryType.CHEST_6_ROW);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return DEFAULT_SLOTS;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 45;
    }

    @Override
    protected int getNextPageSlot() {
        return 53;
    }

    @Override
    protected void layoutBackground(ViewLayout<State> layout, State state, ViewContext ctx) {
    }

    @Override
    protected boolean shouldRenderNavBackground() {
        return false;
    }

    @Override
    protected List<PlayerEntry> getFilteredItems(State state, ViewContext ctx) {
        return TypeReplayViewerLoader.getSession(ctx.player())
                .map(GUIPlayers::collectPlayers)
                .orElse(List.of());
    }

    @Override
    protected ItemStack.Builder renderItem(PlayerEntry entry, int index, HypixelPlayer player) {
        ReplayPlayerEntity replayPlayer = entry.entity();
        Text displayName = getDisplayName(replayPlayer);
        List<Text> lore = List.of(
                Text.of("<7>Health: <f>{}", Math.max(0, Math.round(replayPlayer.getHealth()))),
                Text.empty(),
                Text.of("<e>Left Click to teleport!"),
                Text.of("<e>Right Click for first person!")
        );

        return replayPlayer.getSkin() != null
                ? ItemStacks.head(replayPlayer.getSkin(), 1, displayName, lore)
                : ItemStacks.item(Material.PLAYER_HEAD, 1, displayName, lore);
    }

    @Override
    protected void onItemClick(ClickContext<State> click, ViewContext ctx, PlayerEntry entry, int index) {
        if (click.click() instanceof Click.Right) {
            TypeReplayViewerLoader.getSession(ctx.player())
                    .ifPresent(session -> session.followEntity(ctx.player(), entry.entityId()));
            ctx.player().closeInventory();
            return;
        }

        ctx.player().teleport(entry.entity().getPosition());
    }

    @Override
    protected boolean shouldFilterFromSearch(State state, PlayerEntry item) {
        return false;
    }

    @Override
    protected ItemStack.Builder createPrevPageItem(int currentPage, int totalPages) {
        return ItemStacks.item(Material.ARROW, 1, "<a>Previous Page\n<7>Page <e>{}", currentPage);
    }

    @Override
    protected ItemStack.Builder createNextPageItem(int currentPage, int totalPages) {
        return ItemStacks.item(Material.ARROW, 1, "<a>Next Page\n<7>Page <e>{}", currentPage + 2);
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        var session = TypeReplayViewerLoader.getSession(ctx.player());
        if (session.isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No Replay Session
                    <7>You are not currently watching
                    <7>a replay."""));
        } else if (collectPlayers(session.get()).isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No Players Found
                    <7>No replay players are currently
                    <7>spawned for this timestamp."""));
        }

        Components.back(layout, 49, ctx);
    }

    private static List<PlayerEntry> collectPlayers(ReplaySession session) {
        List<PlayerEntry> entries = new ArrayList<>();
        for (int entityId : session.getEntityManager().getEntityIds()) {
            Entity entity = session.getEntityManager().getEntity(entityId);
            if (entity instanceof ReplayPlayerEntity replayPlayerEntity) {
                entries.add(new PlayerEntry(entityId, replayPlayerEntity));
            }
        }

        entries.sort(Comparator.comparing(entry -> entry.entity().getPlayerName(), String.CASE_INSENSITIVE_ORDER));
        return entries;
    }

    private static Text getDisplayName(ReplayPlayerEntity replayPlayer) {
        try {
            return HypixelPlayer.getDisplayName(replayPlayer.getActualUuid());
        } catch (Exception ignored) {
            return Text.of("<7>{}", replayPlayer.getPlayerName());
        }
    }
}
