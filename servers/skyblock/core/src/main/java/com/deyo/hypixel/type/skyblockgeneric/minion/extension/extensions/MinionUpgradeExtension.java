package com.deyo.hypixel.type.skyblockgeneric.minion.extension.extensions;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUIMinion;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.MinionUpgradeComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.extension.MinionExtension;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinionUpgradeExtension extends MinionExtension {

    public MinionUpgradeExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        super(itemTypeLinker, data);
    }

    @Override
    public @NotNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot) {
        if (getItemTypePassedIn() == null) {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    SkyBlockItem upgradeItem = new SkyBlockItem(p.getInventory().getCursorItem());
                    e.setCancelled(true);

                    ItemType itemTypeLinker = upgradeItem.getAttributeHandler().getPotentialType();
                    if (minion.getExtensionData().hasMinionUpgrade(itemTypeLinker)) {
                        player.sendMessage("<c>This upgrade is already applied to your minion.");
                        e.setCancelled(true);
                        return;
                    }

                    if (upgradeItem.hasComponent(MinionUpgradeComponent.class)) {
                        p.getInventory().setCursorItem(ItemStack.AIR);
                        setItemTypePassedIn(itemTypeLinker);
                        minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                        e.setCancelled(true);
                    } else {
                        player.sendMessage("<c>This item is not a valid Minion Upgrade.");
                        e.setCancelled(true);
                    }
                    new GUIMinion(minion).open(player);
                }

                @Override
                public boolean canPickup() {
                    return true;
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                    return ItemStacks.item(Material.YELLOW_STAINED_GLASS_PANE, 1, """
                            <a>Upgrade Slot
                            <7>You can improve your minion by
                            <7>adding a minion upgrade item
                            <7>here.""");
                }
            };
        } else {
            return new GUIClickableItem(slot) {
                @Override
                public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                    SkyBlockPlayer player = (SkyBlockPlayer) p;
                    if (!p.getInventory().getCursorItem().isAir()) {
                        player.sendMessage("<c>Your cursor must be empty to pick this item up!");
                        e.setCancelled(true);
                        return;
                    }

                    player.addAndUpdateItem(getItemTypePassedIn());
                    setItemTypePassedIn(null);
                    p.getInventory().setCursorItem(ItemStack.AIR);
                    e.setCancelled(true);
                    minion.getExtensionData().setData(slot, MinionUpgradeExtension.this);
                    new GUIMinion(minion).open(player);
                }

                @Override
                public ItemStack.Builder getItem(HypixelPlayer p) {
                    ItemStack.Builder item = new NonPlayerItemUpdater(new SkyBlockItem(getItemTypePassedIn())).getUpdatedItem();
                    ItemStacks.name(item, "<a>Upgrade Slot");
                    item = ItemStacks.lore(item, List.of(
                            Text.of("<7>You can improve your minion by"),
                            Text.of("<7>adding a minion upgrade item"),
                            Text.of("<7>here."),
                            Text.empty(),
                            Text.of("<7>Current Upgrade: <color:{}>{}", getItemTypePassedIn().rarity.getColor(), getItemTypePassedIn().getDisplayName()),
                            Text.empty(),
                            Text.of("<e>Click to remove.")
                    ));

                    return item;
                }
            };
        }
    }

    @Override
    public String toString() {
        if (getItemTypePassedIn() == null)
            return "null";
        return getItemTypePassedIn().name();
    }

    @Override
    public void fromString(String string) {
        if (string.equals("null")) {
            setItemTypePassedIn(null);
            return;
        }
        setItemTypePassedIn(ItemType.valueOf(string));
    }
}
