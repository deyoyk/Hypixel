package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.banker;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.Components;
import com.deyo.hypixel.type.generic.gui.v2.DefaultState;
import com.deyo.hypixel.type.generic.gui.v2.StatelessView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.ViewSession;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.bank.PersonalBankService;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointBankData;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class GUIPersonalVault extends StatelessView {
    private final SkyBlockPlayer target;

    public GUIPersonalVault() {
        this(null);
    }

    public GUIPersonalVault(SkyBlockPlayer target) {
        this.target = target;
    }

    @Override
    public ViewConfiguration<DefaultState> configuration() {
        return new ViewConfiguration<>("Personal Vault", InventoryType.CHEST_4_ROW);
    }

    @Override
    public void layout(ViewLayout<DefaultState> layout, DefaultState state, ViewContext ctx) {
        SkyBlockPlayer viewer = (SkyBlockPlayer) ctx.player();
        SkyBlockPlayer owner = owner(viewer, target);
        DatapointBankData.BankData data = PersonalBankService.data(owner);
        boolean editable = editable(viewer, target);

        if (!data.isPersonalVaultUnlocked()) {
            viewer.sendMessage("<c>That player has not unlocked the Personal Vault!");
            viewer.closeInventory();
            return;
        }

        for (int i = 0; i < 27; i++) {
            SkyBlockItem item = data.getPersonalVault()[i];
            if (editable) {
                layout.editable(i, (s, c) -> item == null || item.isNA() ? ItemStack.AIR.builder()
                    : PlayerItemUpdater.playerUpdate((SkyBlockPlayer) c.player(), item.getItemStack()), (_, _, _, _) -> {
                });
            } else {
                layout.slot(i, (s, c) -> item == null || item.isNA() ? ItemStack.AIR.builder()
                    : PlayerItemUpdater.playerUpdate(viewer, item.getItemStack()));
            }
        }
        for (int i = 27; i < 36; i++)
            layout.slot(i, ItemStacks.named(Material.BLACK_STAINED_GLASS_PANE, ""));
        Components.back(layout, 30, ctx);
        Components.close(layout, 31);
        layout.allowHotkey(editable);
    }

    @Override
    public void onClose(DefaultState state, ViewContext ctx, ViewSession.CloseReason reason) {
        SkyBlockPlayer viewer = (SkyBlockPlayer) ctx.player();
        if (!editable(viewer, target)) return;

        SkyBlockPlayer owner = owner(viewer, target);
        DatapointBankData.BankData data = PersonalBankService.data(owner);
        if (!data.isPersonalVaultUnlocked()) return;

        SkyBlockItem[] items = new SkyBlockItem[27];
        for (int i = 0; i < items.length; i++) {
            SkyBlockItem item = new SkyBlockItem(ctx.inventory().getItemStack(i));
            items[i] = item.isNA() ? null : item;
        }
        data.setPersonalVault(items);
        owner.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.BANK_DATA, DatapointBankData.class).setValue(data);
    }

    static SkyBlockPlayer owner(HypixelPlayer viewer, SkyBlockPlayer target) {
        return target != null ? target : (SkyBlockPlayer) viewer;
    }

    static boolean editable(HypixelPlayer viewer, SkyBlockPlayer target) {
        return target == null || target.getUuid().equals(viewer.getUuid());
    }
}
