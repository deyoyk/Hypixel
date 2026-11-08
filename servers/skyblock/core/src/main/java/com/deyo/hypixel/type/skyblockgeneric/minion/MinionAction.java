package com.deyo.hypixel.type.skyblockgeneric.minion;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.crafting.FurnaceRecipeRegistry;
import com.deyo.hypixel.type.skyblockgeneric.item.components.MinionShippingComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.MinionFuelComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.SellableComponent;
import com.deyo.hypixel.type.skyblockgeneric.minion.extension.MinionExtensionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.extension.MinionExtensions;
import com.deyo.hypixel.type.skyblockgeneric.minion.extension.extensions.MinionShippingExtension;

import java.util.List;

public abstract class MinionAction {

    public abstract @NonNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island);
    public abstract boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island);

    @Getter
    @Setter
    public static class MinionActionEvent {
        private Pos toLook;
        private Runnable action;
    }

    public static void onMinionIteration(IslandMinionData.IslandMinion islandMinion,
                                             SkyBlockMinion minion,
                                             List<SkyBlockItem> items) {
        MinionExtensionData extensionData = islandMinion.getExtensionData();
        SkyBlockItem fuel = extensionData.getFuel();
        double outputMultiplier = fuel == null ? 1.0
                : fuel.getComponent(MinionFuelComponent.class).getOutputMultiplier();

        for (SkyBlockItem item : items) {
            if (extensionData.hasMinionUpgrade(ItemType.AUTO_SMELTER)) {
                item = FurnaceRecipeRegistry.smelt(item).orElse(item);
            }
            if (outputMultiplier > 1.0) {
                item.setAmount((int) Math.floor(item.getAmount() * outputMultiplier));
            }
            boolean hasAdded = islandMinion.addItem(item);
            double sellAmount = !item.hasComponent(SellableComponent.class) ? 0 :
                    item.getComponent(SellableComponent.class).getSellValue();

            if (!hasAdded && sellAmount > 0) {
                MinionShippingExtension shippingExtension = (MinionShippingExtension)
                        extensionData.getOfType(MinionShippingExtension.class);
                if (shippingExtension.getItemTypePassedIn() == null)
                    return;

                SkyBlockItem shippingItem = new SkyBlockItem(shippingExtension.getItemTypePassedIn());
                double percentage = shippingItem.getComponent(MinionShippingComponent.class).getPercentageOfOriginalPrice();
                double sellValue = sellAmount * item.getAmount() * (percentage / 100);
                shippingExtension.addCoins(sellValue, item.getAmount());

                extensionData.setData(MinionExtensions.SHIPPING_SLOT.getSlots()[0], shippingExtension);
            }
        }

        if (extensionData.hasMinionUpgrade(ItemType.DIAMOND_SPREADING)) {
            int baseDrops = items.stream().mapToInt(SkyBlockItem::getAmount).sum();
            int diamonds = 0;
            for (int i = 0; i < baseDrops; i++) {
                if (Math.random() < 0.1) diamonds++;
            }
            if (diamonds > 0) islandMinion.addItem(new SkyBlockItem(ItemType.DIAMOND, diamonds));
        }
    }

}
