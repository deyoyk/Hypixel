package com.deyo.hypixel.type.bedwarsgame.gui;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.shop.ShopItem;
import com.deyo.hypixel.type.bedwarsgame.shop.ShopManager;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;

import java.util.ArrayList;
import java.util.List;

public class GUIQuickBuyEditor extends StatelessView {
    private final ShopManager shopService = TypeBedWarsGameLoader.shopManager;
    private final ShopItem shopItem;
    private final BedWarsGame game;

    public GUIQuickBuyEditor(BedWarsGame game, ShopItem shopItem) {
        this.shopItem = shopItem;
        this.game = game;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Adding to Quick Buy...", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        layout.slot(4, (s, c) -> {
            List<Text> lore = new ArrayList<>();
            lore.add(Text.of("<7>Cost: {}", Text.of("<color:{}>{} {}", shopItem.getCurrency().getColor(),
                    shopItem.getPrice().apply(game.getGameType()), shopItem.getCurrency().getName())));
            lore.add(Text.literal(" "));
            if (shopItem.getDescription() != null && !shopItem.getDescription().isEmpty()) {
                for (String line : StringUtility.splitByNewLine(shopItem.getDescription())) {
                    lore.add(Text.of("<7>{}", Text.parse(line)));
                }
                lore.add(Text.literal(" "));
            }
            lore.add(Text.of("<e>Adding item to Quick Slot!"));

            return ItemStacks.lore(
                    ItemStacks.customName(shopItem.getDisplay().builder(), "<a>{}", shopItem.getName()),
                    lore
            );
        });

        GUIItemShop.populateShopItems(layout, shopService, game, null, shopItem, _ -> {
        });
    }

    @Override
    public boolean onBottomClick(ClickContext<DefaultState> click, ViewContext ctx) {
        return false;
    }
}
