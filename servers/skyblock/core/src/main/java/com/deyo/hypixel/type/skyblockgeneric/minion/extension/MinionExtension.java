package com.deyo.hypixel.type.skyblockgeneric.minion.extension;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class MinionExtension {
    private ItemType itemTypePassedIn;
    private Object data;

    public MinionExtension(@Nullable ItemType itemTypeLinker, @Nullable Object data) {
        this.itemTypePassedIn = itemTypeLinker;
        this.data = data;
    }

    public abstract @NonNull GUIClickableItem getDisplayItem(IslandMinionData.IslandMinion minion, int slot);
    public abstract String toString();
    public abstract void fromString(String string);
}
