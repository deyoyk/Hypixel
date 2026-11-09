package com.deyo.hypixel.type.backwaterbayou.gui;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.shop.ShopPrice;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CoinShopPrice;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CombinedShopPrice;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.ItemShopPrice;

import java.util.List;

public class GUIJunkerJoel extends ShopView {
    private static final int[] TRADE_SLOTS = {10, 11, 12, 19, 20, 21, 29, 30, 31, 32, 34};

    public GUIJunkerJoel() {
        super("Junker Joel", TRADE_SLOTS);
    }

    @Override
    public void initializeShopItems() {
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.JUNK_SINKER), 1,
                new CoinShopPrice(10_000)));
        attachItem(ShopItem.Stackable(new SkyBlockItem(ItemType.TREASURE_BAIT), 16,
                new ItemShopPrice(ItemType.RUSTY_COIN, 16)));

        ShopItem challengeRod = ShopItem.Single(new SkyBlockItem(ItemType.CHALLENGE_ROD), 1,
                combined(new ItemShopPrice(ItemType.FISHING_ROD, 1),
                        new ItemShopPrice(ItemType.RUSTY_COIN, 16)));
        challengeRod.setDisplayName("<a>Challenging Rod");
        attachItem(challengeRod);

        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.JUNK_TALISMAN), 1,
                new ItemShopPrice(ItemType.RUSTY_COIN, 32)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.JUNK_RING), 1,
                combined(new ItemShopPrice(ItemType.JUNK_TALISMAN, 1),
                        new ItemShopPrice(ItemType.BUSTED_BELT_BUCKLE, 4))));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.JUNK_ARTIFACT), 1,
                combined(new ItemShopPrice(ItemType.JUNK_RING, 1),
                        new ItemShopPrice(ItemType.OLD_LEATHER_BOOT, 1))));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.STINGY_SINKER), 1,
                new ItemShopPrice(ItemType.RUSTY_COIN, 64)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.SPEEDY_LINE), 1,
                new ItemShopPrice(ItemType.BUSTED_BELT_BUCKLE, 8)));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.BRONZE_SHIP_ENGINE), 1,
                combined(new ItemShopPrice(ItemType.RUSTY_COIN, 64),
                        new ItemShopPrice(ItemType.BUSTED_BELT_BUCKLE, 8))));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.GOLD_BOTTLE_CAP), 1,
                combined(new ItemShopPrice(ItemType.RUSTY_COIN, 512),
                        new ItemShopPrice(ItemType.BUSTED_BELT_BUCKLE, 64),
                        new ItemShopPrice(ItemType.OLD_LEATHER_BOOT, 8))));
        attachItem(ShopItem.Single(new SkyBlockItem(ItemType.BAYOU_TRAVEL_SCROLL), 1,
                new ItemShopPrice(ItemType.BUSTED_BELT_BUCKLE, 8)));
    }

    private static ShopPrice combined(ShopPrice... prices) {
        return new CombinedShopPrice(List.of(prices));
    }

    @Override
    protected void layoutBackground(ViewLayout<State> layout, State state, ViewContext ctx) {
        for (int slot : TRADE_SLOTS) {
            layout.slot(slot, ItemStack.AIR.builder());
        }
    }

    @Override
    protected int getNextPageSlot() {
        return -1;
    }

    @Override
    protected int getPreviousPageSlot() {
        return -1;
    }

    @Override
    protected void layoutCustom(ViewLayout<State> layout, State state, ViewContext ctx) {
        renderLockedItems(layout);
        super.layoutCustom(layout, state, ctx);
    }

    private static void renderLockedItems(ViewLayout<State> layout) {
        locked(layout, 13, ItemStacks.head(
                "f6f6c3ebab908a184d49a0f8c85edd3ed48a65d69213bc0367db67a7a1c0c3a7",
                """
                        <a>Backwater Helmet
                        <7>Health: <c>+30
                        <7>Defense: <a>+15
                        <7>Sea Creature Chance: <3>+1%
                        <7>Treasure Chance: <6>+0.1%

                        <8>Tiered Bonus: Swamp Soldier (0/8)
                        <7>Increases damage dealt to <3>Sea
                        <3>Creatures <7>by <a>5%</a>.

                        <8>This item can be reforged!
                        <a><l>UNCOMMON HELMET</l>

                        <7>Cost
                        <f>Angler Helmet
                        <a>Rusty Coin <8>x4

                        <e>Click to trade!"""
        ));
        locked(layout, 14, ItemStacks.item(Material.LEATHER_CHESTPLATE, """
                <a>Backwater Chestplate
                <7>Health: <c>+50
                <7>Defense: <a>+25
                <7>Sea Creature Chance: <3>+1%
                <7>Treasure Chance: <6>+0.1%

                <8>Tiered Bonus: Swamp Soldier (0/8)
                <7>Increases damage dealt to <3>Sea
                <3>Creatures <7>by <a>5%</a>.

                <8>This item can be reforged!
                <a><l>UNCOMMON CHESTPLATE</l>

                <7>Cost
                <f>Angler Chestplate
                <a>Rusty Coin <8>x4

                <e>Click to trade!"""));
        locked(layout, 15, ItemStacks.item(Material.LEATHER_LEGGINGS, """
                <a>Backwater Leggings
                <7>Health: <c>+40
                <7>Defense: <a>+20
                <7>Sea Creature Chance: <3>+1%
                <7>Treasure Chance: <6>+0.1%

                <8>Tiered Bonus: Swamp Soldier (0/8)
                <7>Increases damage dealt to <3>Sea
                <3>Creatures <7>by <a>5%</a>.

                <8>This item can be reforged!
                <a><l>UNCOMMON LEGGINGS</l>

                <7>Cost
                <f>Angler Leggings
                <a>Rusty Coin <8>x4

                <e>Click to trade!"""));
        locked(layout, 16, ItemStacks.item(Material.LEATHER_BOOTS, """
                <a>Backwater Boots
                <7>Health: <c>+20
                <7>Defense: <a>+10
                <7>Sea Creature Chance: <3>+1%
                <7>Treasure Chance: <6>+0.1%

                <8>Tiered Bonus: Swamp Soldier (0/8)
                <7>Increases damage dealt to <3>Sea
                <3>Creatures <7>by <a>5%</a>.

                <8>This item can be reforged!
                <a><l>UNCOMMON BOOTS</l>

                <7>Cost
                <f>Angler Boots
                <a>Rusty Coin <8>x4

                <e>Click to trade!"""));
        locked(layout, 22, ItemStacks.head(
                "ab6f8eea74ca22c2ab64592bab2699df39c3e7c1db7d2c9fc687be0dc8c7f1ed",
                """
                        <a>Backwater Necklace
                        <7>Fishing Speed: <b>+2
                        <7>Sea Creature Chance: <3>+1%
                        <7>Treasure Chance: <6>+0.1%

                        <8>Tiered Bonus: Swamp Soldier (0/8)
                        <7>Increases damage dealt to <3>Sea
                        <3>Creatures <7>by <a>5%</a>.

                        <8>This item can be reforged!
                        <a><l>UNCOMMON NECKLACE</l>

                        <7>Cost
                        <f>Angler Necklace
                        <a>Rusty Coin <8>x4

                        <e>Click to trade!"""
        ));
        locked(layout, 23, ItemStacks.head(
                "f2adeecbf20b58fd4cad8aaa3b4653d7165e07aa167be48a321b096d56a9fe35",
                """
                        <a>Backwater Cloak
                        <7>Fishing Speed: <b>+2
                        <7>Sea Creature Chance: <3>+1%
                        <7>Treasure Chance: <6>+0.1%

                        <8>Tiered Bonus: Swamp Soldier (0/8)
                        <7>Increases damage dealt to <3>Sea
                        <3>Creatures <7>by <a>5%</a>.

                        <8>This item can be reforged!
                        <a><l>UNCOMMON CLOAK</l>

                        <7>Cost
                        <f>Angler Cloak
                        <a>Rusty Coin <8>x4

                        <e>Click to trade!"""
        ));
        locked(layout, 24, ItemStacks.head(
                "3c150be849a39208a38a83c5605e79aef93a12b37072b931693990192cb77a19",
                """
                        <a>Backwater Belt
                        <7>Fishing Speed: <b>+2
                        <7>Sea Creature Chance: <3>+1%
                        <7>Treasure Chance: <6>+0.1%

                        <8>Tiered Bonus: Swamp Soldier (0/8)
                        <7>Increases damage dealt to <3>Sea
                        <3>Creatures <7>by <a>5%</a>.

                        <8>This item can be reforged!
                        <a><l>UNCOMMON BELT</l>

                        <7>Cost
                        <f>Angler Belt
                        <a>Rusty Coin <8>x4

                        <e>Click to trade!"""
        ));
        locked(layout, 25, ItemStacks.head(
                "9a6c887b86b2a59fdea5052413eab74d434c07f52231ce7ac33af5b395beded0",
                """
                        <a>Backwater Gloves
                        <7>Fishing Speed: <b>+2
                        <7>Sea Creature Chance: <3>+1%
                        <7>Treasure Chance: <6>+0.1%

                        <8>Tiered Bonus: Swamp Soldier (0/8)
                        <7>Increases damage dealt to <3>Sea
                        <3>Creatures <7>by <a>5%</a>.

                        <8>This item can be reforged!
                        <a><l>UNCOMMON GLOVES</l>

                        <7>Cost
                        <f>Angler Bracelet
                        <a>Rusty Coin <8>x4

                        <e>Click to trade!"""
        ));
        locked(layout, 28, ItemStacks.head(
                "26629dfa3fdfef04054024e0156d5e19da5401b1911f59b4bd3982685fe54c2c",
                """
                        <7>[Lvl 100] <f>Hermit Crab
                        <8>Fishing Pet

                        <7>Defense: <a>+20
                        <7>Fishing Speed: <b>+20
                        <7>Sea Creature Chance: <3>+2%

                        <6>Comfort Zone
                        <7>Grants <b>+20☂ Fishing Speed</b> for <a>30s
                        <7>upon catching <6>Treasure</6>.

                        <c>This is a preview of Lvl 100.
                        <c>New pets are lowest level!

                        <7>Cost
                        <a>Rusty Coin <8>x32

                        <e>Click to trade!"""
        ));
        locked(layout, 33, ItemStacks.head(
                "9809753cbab0380c7a1c18925faf9b51e44caadd1e5748542b0f23835f4ef64e",
                """
                        <9>Treasure Hook
                        <7>Only allows you to catch items and
                        <6>Treasure<7>.

                        <7>Talk to <2>Roddy</2> in the <2>Backwater
                        <2>Bayou <7>to apply this <9>Hook</9> to a Fishing
                        <7>Rod.

                        <4>❣ <c>Requires <a>Fishing Skill 25</a>.
                        <9><l>RARE ROD PART</l>

                        <7>Cost
                        <6>Bayou Water Orb
                        <5>Old Leather Boot <8>x4
                        <9>Busted Belt Buckle <8>x32
                        <a>Rusty Coin <8>x256

                        <c>Not unlocked!"""
        ));
    }

    private static void locked(ViewLayout<State> layout, int slot, ItemStack.Builder item) {
        layout.slot(slot, item);
    }
}
