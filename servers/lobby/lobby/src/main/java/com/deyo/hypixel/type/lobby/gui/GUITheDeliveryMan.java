package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.utility.WebLinks;

public class GUITheDeliveryMan extends StatelessView {

    private static final String MYSTERY_DUST_DELIVERY = """
            <c>Mystery Dust Delivery
            <7>You already picked up this delivery,
            <7>come back later!

            <7>Next Delivery: 0d 0h 0m 0s""";

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("The Delivery Man", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        for (int slot = 20; slot <= 24; slot++) {
            layout.slot(slot, ItemStacks.item(Material.MINECART, MYSTERY_DUST_DELIVERY),
                (_, c) -> c.player().sendMessage("<c>You already picked up this delivery, come back later!"));
        }
        layout.slot(29, ItemStacks.item(Material.CHEST, """
                <c>Survey
                <7>There isn't a survey available right
                <7>now!"""));
        layout.slot(30, ItemStacks.item(Material.CHEST_MINECART, """
                <6>Social Media Rewards
                <7>Click to view all available Social Media
                <7>Rewards!"""),
            (_, c) -> WebLinks.sendSocialMediaRewardsLink(c.player()));
        layout.slot(31, ItemStacks.item(Material.GOLD_BLOCK, """
                <a>Daily Reward

                <7>Daily rewards for visiting our
                <7>website including: <6>Coins<7>, <3>Hypixel
                <3>Experience<7>, <b>SkyWars Souls<7>, <c>Unique
                <c>Cosmetics <7>and more!

                <7>Current Streak: <b>0

                <e>Click here to get the link in chat!"""),
            (_, c) -> WebLinks.sendDailyRewardLink(c.player()));
        layout.slot(32, ItemStacks.item(Material.MINECART, """
                <6>Website Link
                <7>You have linked your account to the
                <7>forums."""),
            (_, c) -> WebLinks.sendForumsLink(c.player()));
        layout.slot(33, ItemStacks.item(Material.CHEST_MINECART, """
                <a>Daily Reward
                <7>Free 2,200 Network Experience
                <7>and 3,000 Arcade Coins!

                <e>Click here to claim!"""),
            (_, c) -> WebLinks.sendDailyRewardLink(c.player()));
        layout.slot(40, ItemStacks.item(Material.MINECART, """
                <c>Hardware Survey
                <7>You have already completed this
                <7>Hardware Survey, thank you!"""),
            (_, c) -> c.player().sendMessage("<c>You have already completed this Hardware Survey, thank you!"));
    }
}
