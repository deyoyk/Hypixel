package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.*;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.NewAccessoryLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class GUIStarterAccessories extends StatelessView {
    private static final int[] DISPLAY_SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private final List<NewAccessoryLevelCause> causes;

    public GUIStarterAccessories(ItemType... causes) {
        this.causes = Stream.of(causes).map(SkyBlockLevelCause::getAccessoryCause).toList();
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Starter -> Accessories", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        Components.fill(layout);
        Components.close(layout, 49);
        Components.back(layout, 48, ctx);

        SkyBlockPlayer player = (SkyBlockPlayer) ctx.player();
        Set<ItemType> collected = new HashSet<>(player.getTalismans());

        for (int index = 0; index < causes.size() && index < DISPLAY_SLOTS.length; index++) {
            NewAccessoryLevelCause cause = causes.get(index);
            if (cause == null) {
                continue;
            }

            ItemType itemType = cause.itemTypeLinker;
            int slot = DISPLAY_SLOTS[index];

            layout.slot(slot, (s, c) -> ItemStacks.lore(
                    new SkyBlockItem(itemType).getItemStackBuilder(),
                    accessoryLore(collected.contains(itemType))
            ));
        }
    }

    static List<Text> accessoryLore(boolean collected) {
        return List.of(
                Text.empty(),
                collected ? Text.of("<a>✔ Collected") : Text.of("<c>✖ Not collected"),
                Text.of("<7>Collect this accessory to earn <b>+1 SkyBlock XP<7>!")
        );
    }
}
