package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildPermission;
import com.deyo.hypixel.commons.guild.GuildRank;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.View;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class GUIGuildPermissions implements View<GUIGuildPermissions.State> {
    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return new ViewConfiguration<>("Guild Permissions", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        List<GuildRank> ranks = new ArrayList<>(state.guild().getRanks());
        ranks.sort(Comparator.naturalOrder());

        for (int i = 0; i < ranks.size() && i < SLOTS.length; i++) {
            GuildRank rank = ranks.get(i);
            layout.slot(SLOTS[i], (s, c) -> {
                boolean editable = rank.getPriority() > 0
                        && state.guild().getMasterUuid().equals(c.player().getUuid());
                List<Text> lore = new ArrayList<>();
                lore.add(Text.of("<7>Priority: <e>{}", rank.getPriority()));
                lore.add(Text.empty());
                lore.add(Text.of(editable ? "<e>Click to edit permissions!" : "<c>This rank cannot be edited."));
                return ItemStacks.item(editable ? Material.LIME_DYE : Material.GRAY_DYE, 1,
                        Text.of("<a>{}", rank.getName()), lore);
            }, (click, c) -> {
                if (rank.getPriority() == 0 || !state.guild().getMasterUuid().equals(c.player().getUuid())) {
                    c.player().sendMessage("<c>Only the Guild Master can edit rank permissions!");
                    return;
                }
                c.push(new GUIGuildRankPermissions(),
                        new GUIGuildRankPermissions.State(rank.getName(), permissionsOf(rank)));
            });
        }
    }

    static Set<GuildPermission> permissionsOf(GuildRank rank) {
        if (rank.getPermissions() == null) return Set.of();
        EnumSet<GuildPermission> permissions = EnumSet.noneOf(GuildPermission.class);
        permissions.addAll(rank.getPermissions());
        return permissions;
    }

    record State(GuildData guild) {
    }
}
