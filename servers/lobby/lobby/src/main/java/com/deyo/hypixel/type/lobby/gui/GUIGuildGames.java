package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.View;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.guild.GuildManager;
import com.deyo.hypixel.type.lobby.game.GameType;

import java.util.ArrayList;
import java.util.List;

public class GUIGuildGames implements View<GUIGuildGames.State> {
    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Guild Games", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        GameType[] games = GameType.values();
        for (int i = 0; i < games.length && i < SLOTS.length; i++) {
            GameType game = games[i];
            layout.slot(SLOTS[i], (s, c) -> {
                boolean selected = state.games().contains(game.name());
                List<Text> lore = new ArrayList<>(game.getLore());
                lore.add(Text.empty());
                lore.add(Text.of(selected ? "<a>Selected" : "<c>Not selected"));
                lore.add(Text.of("<e>Click to toggle!"));
                return ItemStacks.name(ItemStacks.appendLore(ItemStacks.copy(game.getItem().build()), lore),
                        "<a>{}", game.getDisplayName());
            }, (click, c) -> {
                List<String> updated = toggled(state.games(), game);
                c.session(State.class).setState(new State(updated));
                GuildManager.changeSetting(c.player(), "games", String.join(",", updated));
            });
        }
    }

    static List<String> toggled(List<String> games, GameType game) {
        List<String> updated = new ArrayList<>(games);
        if (!updated.remove(game.name())) {
            updated.add(game.name());
        }
        return List.copyOf(updated);
    }

    record State(List<String> games) {
        State {
            games = List.copyOf(games);
        }
    }
}
