package com.deyo.hypixel.type.skyblockgeneric.entity;

import lombok.Getter;
import com.deyo.hypixel.type.generic.entity.drop.VanillaItemEntity;
import com.deyo.hypixel.type.generic.utility.BoundedList;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class DroppedItemEntityImpl extends VanillaItemEntity {
    @Getter
    private static final Map<SkyBlockPlayer, BoundedList<DroppedItemEntityImpl>> droppedItems = new ConcurrentHashMap<>();
    private final SkyBlockPlayer player;

    public DroppedItemEntityImpl(SkyBlockItem item, SkyBlockPlayer player) {
        super(new NonPlayerItemUpdater(item.getItemStack()).getUpdatedItem().build());

        this.player = player;

        setAutoViewable(false);

        droppedItems.computeIfAbsent(player,
                        key -> new BoundedList<>(50, DroppedItemEntityImpl::isRemoved, DroppedItemEntityImpl::remove))
                .add(this);
    }

    public static void clearDroppedItems(SkyBlockPlayer player) {
        BoundedList<DroppedItemEntityImpl> items = droppedItems.remove(player);
        if (items == null) return;
        items.snapshot().stream().filter(item -> !item.isRemoved()).forEach(DroppedItemEntityImpl::remove);
        items.clear();
    }

    @Override
    public void spawn() {
        super.spawn();
        addViewer(player);
    }

    @Override
    protected boolean canMergeWith(VanillaItemEntity other) {
        return other instanceof DroppedItemEntityImpl dropped && dropped.player == this.player;
    }

    public SkyBlockItem getItem() {
        return new SkyBlockItem(getItemStack());
    }
}
