package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.TrackedItem;
import com.deyo.hypixel.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocol;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelPaginatedGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.utility.PaginationList;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumDisplays;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumableItemCategory;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.ItemPriceCalculator;

import java.util.List;
import java.util.UUID;

public class GUIMuseumCategory extends HypixelPaginatedGUI<ItemType> {
    private final MuseumableItemCategory category;
    private final SkyBlockPlayer target;

    public GUIMuseumCategory(MuseumableItemCategory category) {
        this(category, null);
    }

    public GUIMuseumCategory(MuseumableItemCategory category, SkyBlockPlayer target) {
        super(InventoryType.CHEST_6_ROW);

        this.category = category;
        this.target = target;
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);

        ItemStack item = e.getClickedItem();
        SkyBlockItem skyBlockItem = new SkyBlockItem(item);

        if (skyBlockItem.getAttributeHandler().getPotentialType() == null) {
            return;
        }

        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();
        if (!GUIYourMuseum.canModify(player, target)) {
            player.sendMessage("<c>You can only modify your own museum!");
            return;
        }
        DatapointMuseum.MuseumData data = player.getMuseumData();

        if (data.getItemInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            player.sendMessage(Text.key("gui_museum.category.already_in_museum", skyBlockItem.getDisplayName()));
            return;
        }

        if (data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType()) != null) {
            UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
            UUID previouslyInMuseumUUID = UUID.fromString(
                    data.getItemPreviouslyInMuseum(skyBlockItem.getAttributeHandler().getPotentialType())
                            .getAttributeHandler().getUniqueTrackedID()
            );

            if (!trackedItemUUID.equals(previouslyInMuseumUUID)) {
                player.sendMessage(Text.key("gui_museum.category.can_only_read"));
                return;
            }
        }

        if (category.contains(skyBlockItem.getAttributeHandler().getPotentialType())) {
            skyBlockItem.getAttributeHandler().setSoulBound(true);
            data.add(skyBlockItem);
            player.setMuseumData(data);
            player.getInventory().setItemStack(e.getSlot(), ItemStack.AIR);
            player.closeInventory();
            MuseumDisplays.updateDisplay(player);

            new GUIMuseumCategory(category, target).open(player);
            player.sendMessage(Text.key("gui_museum.category.donated", skyBlockItem.getDisplayName()));
        }
    }

    @Override
    public int[] getPaginatedSlots() {
        return new int[]{
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
        };
    }

    @Override
    public PaginationList<ItemType> fillPaged(HypixelPlayer player, PaginationList<ItemType> paged) {
        paged.addAll(category.getItems());
        return paged;
    }

    @Override
    public boolean shouldFilterFromSearch(String query, ItemType item) {
        return !item.getDisplayName().toLowerCase().contains(query.toLowerCase());
    }

    @Override
    public void performSearch(HypixelPlayer p, String query, int page, int maxPage) {
        SkyBlockPlayer player = (SkyBlockPlayer) p;
        if (!GUIYourMuseum.canModify(player, target)) {
            player.sendMessage("<c>You can only search your own museum!");
            return;
        }
        border(ItemStacks.named(Material.BLACK_STAINED_GLASS_PANE, ""));
        set(GUIClickableItem.getCloseItem(49));
        set(createSearchItem(this, 50, query));
        set(GUIClickableItem.getGoBackItem(48, new GUIYourMuseum(target)));

        if (page > 1) {
            set(createNavigationButton(this, 45, query, page, false));
        }
        if (page < maxPage) {
            set(createNavigationButton(this, 53, query, page, true));
        }

        for (int i = 0; i < 36; i++) {
            SkyBlockItem item = new SkyBlockItem(player.getInventory().getItemStack(i));
            if (item.getAttributeHandler().getPotentialType() == null) {
                continue;
            }

            if (category.contains(item.getAttributeHandler().getPotentialType())) {
                TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage(
                        UUID.fromString(item.getAttributeHandler().getUniqueTrackedID())
                );
                TrackedItem trackedItem = ((TrackedItemRetrieveProtocol.TrackedItemResponse) new ProxyService(ServiceType.ITEM_TRACKER).handleRequest(message).join()).trackedItem();

                ItemStack.Builder toReturn = item.getItemStackBuilder();
                List<Text> lore = item.getLoreText(player);
                lore.add(Text.of("<8><m>---------------------"));
                lore.add(Text.key("gui_museum.category.item_created_label"));
                lore.add(Text.of("<a>{}", StringUtility.formatAsDate(trackedItem.getCreated())));
                lore.add(Text.literal(" "));
                lore.add(Text.key("gui_museum.category.click_to_donate"));

                player.getInventory().setItemStack(i, ItemStacks.name(ItemStacks.lore(toReturn, lore),
                        Text.literal(item.getDisplayName())).build());
            }
        }
    }

    @Override
    public Text getTitleText(HypixelPlayer player, String query, int page, PaginationList<ItemType> paged) {
        return Text.key("gui_museum.category.title", category.toString());
    }

    @Override
    public GUIClickableItem createItemFor(ItemType item, int slot, HypixelPlayer p) {
        SkyBlockPlayer player = GUIYourMuseum.owner(p, target);
        DatapointMuseum.MuseumData data = player.getMuseumData();
        SkyBlockItem skyBlockItem = data.getItem(category, item);
        boolean inMuseum = skyBlockItem != null;
        boolean hasTakenItOut = data.getPreviouslyInMuseum().contains(skyBlockItem);

        return new GUIClickableItem(slot) {
            @Override
            public void run(InventoryPreClickEvent e, HypixelPlayer p) {
                SkyBlockPlayer player = (SkyBlockPlayer) p;
                if (!GUIYourMuseum.canModify(player, target)) {
                    player.sendMessage("<c>You can only modify your own museum!");
                    return;
                }
                if (!inMuseum || hasTakenItOut) {
                    return;
                }

                player.sendMessage(Text.key("gui_museum.category.retrieved_message", item.getDisplayName()));
                player.sendMessage(Text.key("gui_museum.category.retrieved_return_message"));

                data.moveToRetrieved(skyBlockItem);
                player.setMuseumData(data);
                MuseumDisplays.updateDisplay(player);

                player.closeInventory();
                player.addAndUpdateItem(skyBlockItem.getItemStack());
                new GUIMuseumCategory(category, target).open(player);
            }

            @Override
            public ItemStack.Builder getItem(HypixelPlayer p) {
                if (!inMuseum) {
                    return ItemStacks.item(Material.GRAY_DYE, 1,
                            Text.key("gui_museum.category.item_not_in_museum", item.getDisplayName()),
                            Text.keyLines("gui_museum.category.item_not_in_museum.lore"));
                }

                UUID trackedItemUUID = UUID.fromString(skyBlockItem.getAttributeHandler().getUniqueTrackedID());
                TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage message = new TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage(trackedItemUUID);
                TrackedItem trackedItem = ((TrackedItemRetrieveProtocol.TrackedItemResponse) new ProxyService(ServiceType.ITEM_TRACKER)
                        .handleRequest(message).join()).trackedItem();

                List<Text> lore = skyBlockItem.getLoreText();
                lore.add(Text.of("<8><m>---------------------"));
                lore.add(Text.key("gui_museum.category.item_donated_label"));
                lore.add(Text.of("<b>{}", StringUtility.formatAsDate(data.getInsertionTimes().get(trackedItemUUID))));
                lore.add(Text.literal(" "));
                lore.add(Text.key("gui_museum.category.item_created_label"));
                lore.add(Text.of("<a>{}", StringUtility.formatAsDate(trackedItem.getCreated())));
                lore.add(Text.of("<6>  {} <7>created", StringUtility.commaifyAndTh(trackedItem.getNumberMade())));
                lore.add(Text.literal(" "));
                lore.add(Text.key("gui_museum.category.item_clean_value_label"));
                lore.add(Text.of("<6>{:,} Coins", new ItemPriceCalculator(skyBlockItem).calculateCleanPrice()));
                lore.add(Text.literal(" "));
                lore.add(Text.key("gui_museum.category.item_value_label"));
                if (data.getCalculatedPrices().containsKey(trackedItemUUID)) {
                    lore.add(Text.of("<6>{:,} Coins", data.getCalculatedPrices().get(trackedItemUUID)));
                } else {
                    lore.add(Text.key("gui_museum.category.uncalculated"));
                }
                lore.add(Text.literal(" "));
                lore.add(Text.key("gui_museum.category.display_slot_label"));
                DatapointMuseum.DisplayPlacement placement = data.getDisplayHandler().getItemDisplayPlacement(skyBlockItem);
                if (data.getCurrentlyInMuseum().contains(skyBlockItem) && placement != null) {
                    lore.add(Text.of("<9>{} Slot #{}", placement.display(), placement.slot() + 1));
                } else {
                    lore.add(Text.key("gui_museum.category.not_in_display"));
                }
                if (hasTakenItOut) {
                    lore.add(Text.of("<8><m>---------------------"));
                    lore.addAll(Text.keyLines("gui_museum.category.retrieved_from_museum.lore"));
                } else {
                    lore.add(Text.literal(" "));
                    lore.add(Text.key("gui_museum.category.click_to_retrieve"));
                }

                return ItemStacks.item(hasTakenItOut ? Material.LIME_DYE : item.material, 1,
                        Text.of("<a>{}", item.getDisplayName()), lore);
            }
        };
    }


    @Override
    public void onClose(InventoryCloseEvent e, CloseReason reason) {
        SkyBlockPlayer player = (SkyBlockPlayer) e.getPlayer();

        SkyBlockDataHandler.Data.INVENTORY.onLoad.accept(
                player, SkyBlockDataHandler.Data.INVENTORY.onQuit.apply(player)
        );
    }
}
