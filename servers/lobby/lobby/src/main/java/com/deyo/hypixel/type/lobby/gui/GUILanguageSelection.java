package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLocale;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.Arrays;
import java.util.List;

public class GUILanguageSelection extends HypixelInventoryGUI {
    private static final int[] LOCALE_SLOTS = {10, 11, 12, 13, 14, 15, 16};

    public GUILanguageSelection() {
        super("Language Selection", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        fill(FILLER_ITEM);
        set(GUIClickableItem.getGoBackItem(31, new GUIMyProfile()));
        set(GUIClickableItem.getCloseItem(35));

        HypixelPlayer player = e.player();
        DatapointLocale.SupportedLocale current = currentLocale(player);
        List<DatapointLocale.SupportedLocale> locales = selectableLocales();

        for (int index = 0; index < locales.size() && index < LOCALE_SLOTS.length; index++) {
            DatapointLocale.SupportedLocale locale = locales.get(index);
            boolean selected = locale == current;

            set(new GUIClickableItem(LOCALE_SLOTS[index]) {
                @Override
                public ItemStack.Builder getItem(HypixelPlayer viewer) {
                    return ItemStacks.head(locale.getIcon(), 1, """
                            <a>{}
                            <7>Language: <f>{}

                            {}""",
                            locale.getName(),
                            locale.getLocale().getDisplayName(),
                            selected ? "<a>✔ Currently selected" : "<e>Click to select!");
                }

                @Override
                public void run(InventoryPreClickEvent click, HypixelPlayer clicker) {
                    if (selected) return;
                    clicker.updateLocale(locale);
                    clicker.sendMessage("<a>Your language has been set to <e>{}</a>.", locale.getName());
                    new GUILanguageSelection().open(clicker);
                }
            });
        }

        updateItemStacks(getInventory(), getPlayer());
    }

    static List<DatapointLocale.SupportedLocale> selectableLocales() {
        return Arrays.stream(DatapointLocale.SupportedLocale.values())
                .filter(locale -> locale != DatapointLocale.SupportedLocale.UNSET)
                .toList();
    }

    static DatapointLocale.SupportedLocale currentLocale(HypixelPlayer player) {
        DatapointLocale.SupportedLocale current = player.getDataHandler()
                .get(HypixelDataHandler.Data.LOCALE, DatapointLocale.class)
                .getValue()
                .getCurrentLocale();
        return current == DatapointLocale.SupportedLocale.UNSET
                ? DatapointLocale.SupportedLocale.ENGLISH
                : current;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
