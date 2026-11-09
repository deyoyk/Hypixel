package com.deyo.hypixel.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GUIBedWarsTeleporter extends StatelessView {
    private static final int[] TELEPORT_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25
    };

    private final Collection<BedWarsPlayer> players;

    public GUIBedWarsTeleporter(Collection<BedWarsPlayer> players) {
        this.players = players;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Teleporter", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.allowHotkey(false);
        Components.fill(layout);
        Components.close(layout, 31);

        List<BedWarsPlayer> targets = teleportTargets(players, ctx.player().getUuid());
        if (targets.isEmpty()) {
            layout.slot(22, ItemStacks.item(Material.BARRIER, 1, """
                    <c>No players to teleport to
                    <7>All other players have been eliminated."""));
            return;
        }

        for (int index = 0; index < targets.size() && index < TELEPORT_SLOTS.length; index++) {
            BedWarsPlayer target = targets.get(index);
            layout.slot(TELEPORT_SLOTS[index],
                    (_, _) -> ItemStacks.head(target.getPlayerSkin(), Text.of("<f>{}", target.getUsername()), List.of(
                            Text.of("<7>Click to teleport to this player."))),
                    (_, click) -> {
                        click.player().teleport(target.getPosition());
                        click.player().closeInventory();
                    });
        }
    }

    static List<BedWarsPlayer> teleportTargets(Collection<BedWarsPlayer> players, UUID viewerId) {
        return players.stream()
                .filter(player -> !player.getUuid().equals(viewerId))
                .filter(player -> !Boolean.TRUE.equals(player.getTag(BedWarsGame.ELIMINATED_TAG)))
                .toList();
    }
}
