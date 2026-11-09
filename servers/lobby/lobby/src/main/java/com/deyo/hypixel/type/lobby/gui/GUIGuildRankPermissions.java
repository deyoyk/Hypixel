package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.guild.GuildPermission;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.View;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.guild.GuildManager;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class GUIGuildRankPermissions implements View<GUIGuildRankPermissions.State> {
    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    @Override
    public ViewConfiguration<State> configuration() {
        return ViewConfiguration.withString((state, ctx) -> "Guild Permissions - " + state.rankName(),
                InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<State> layout, State state, ViewContext ctx) {
        Components.fill(layout);
        Components.back(layout, 31, ctx);

        GuildPermission[] permissions = GuildPermission.values();
        for (int i = 0; i < permissions.length && i < SLOTS.length; i++) {
            GuildPermission permission = permissions[i];
            layout.slot(SLOTS[i], (s, c) -> {
                boolean enabled = state.permissions().contains(permission);
                List<Text> lore = List.of(
                        Text.of(enabled ? "<a>Enabled" : "<c>Disabled"),
                        Text.empty(),
                        Text.of("<e>Click to toggle!")
                );
                return ItemStacks.item(enabled ? Material.LIME_DYE : Material.GRAY_DYE, 1,
                        Text.of("<a>{}", StringUtility.toNormalCase(permission.name())), lore);
            }, (click, c) -> {
                boolean enabled = !state.permissions().contains(permission);
                c.session(State.class).setState(new State(state.rankName(),
                        toggled(state.permissions(), permission, enabled)));
                GuildManager.changeRankPermission(c.player(), state.rankName(), permission, enabled);
            });
        }
    }

    static Set<GuildPermission> toggled(Set<GuildPermission> permissions,
                                        GuildPermission permission, boolean enabled) {
        EnumSet<GuildPermission> updated = EnumSet.noneOf(GuildPermission.class);
        updated.addAll(permissions);
        if (enabled) {
            updated.add(permission);
        } else {
            updated.remove(permission);
        }
        return updated;
    }

    record State(String rankName, Set<GuildPermission> permissions) {
        State {
            permissions = Set.copyOf(permissions);
        }
    }
}
