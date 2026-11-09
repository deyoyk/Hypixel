package com.deyo.hypixel.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

public class GUIBedWarsSpectatorSettings extends StatelessView {
    private static final int MIN_FLY_SPEED = 1;
    private static final int MAX_FLY_SPEED = 5;

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Spectator Settings", InventoryType.CHEST_3_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.allowHotkey(false);
        Components.fill(layout);
        Components.close(layout, 22);

        BedWarsPlayer player = (BedWarsPlayer) ctx.player();

        layout.slot(11, (_, _) -> ItemStacks.item(
                        player.isSpectatorNightVision() ? Material.LIME_DYE : Material.GRAY_DYE, 1, """
                                <a>Night Vision
                                <7>Current: {}
                                <7>Toggle night vision while spectating.

                                <e>Click to toggle!""",
                        player.isSpectatorNightVision() ? "<a>Enabled" : "<c>Disabled"),
                (_, click) -> {
                    player.setSpectatorNightVision(!player.isSpectatorNightVision());
                    applyNightVision(player);
                    click.session(DefaultState.class).refresh();
                });

        layout.slot(15, (_, _) -> ItemStacks.item(Material.FEATHER, 1, """
                        <a>Fly Speed
                        <7>Current: <e>{}x
                        <7>Cycle your spectator flying speed.

                        <e>Click to change!""",
                        player.getSpectatorFlySpeed()),
                (_, click) -> {
                    player.setSpectatorFlySpeed(nextFlySpeed(player.getSpectatorFlySpeed()));
                    applyFlySpeed(player);
                    click.session(DefaultState.class).refresh();
                });
    }

    static int nextFlySpeed(int current) {
        if (current < MIN_FLY_SPEED || current >= MAX_FLY_SPEED) return MIN_FLY_SPEED;
        return current + 1;
    }

    static float minestomFlySpeed(int userSpeed) {
        return Math.clamp(userSpeed, MIN_FLY_SPEED, 10) * 0.1f;
    }

    static void applyNightVision(BedWarsPlayer player) {
        if (player.isSpectatorNightVision()) {
            player.addEffect(new Potion(PotionEffect.NIGHT_VISION, (byte) 0, -1));
        } else {
            player.removeEffect(PotionEffect.NIGHT_VISION);
        }
    }

    static void applyFlySpeed(BedWarsPlayer player) {
        player.setFlyingSpeed(minestomFlySpeed(player.getSpectatorFlySpeed()));
    }
}
