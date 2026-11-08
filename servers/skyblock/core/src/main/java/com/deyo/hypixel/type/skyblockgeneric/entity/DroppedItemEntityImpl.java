package com.deyo.hypixel.type.skyblockgeneric.entity;

import lombok.Getter;
import com.deyo.hypixel.type.generic.entity.drop.VanillaItemEntity;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class DroppedItemEntityImpl extends VanillaItemEntity {
    @Getter
    private static final Map<SkyBlockPlayer, List<DroppedItemEntityImpl>> droppedItems = new HashMap<>();
    private final SkyBlockPlayer player;

    public DroppedItemEntityImpl(SkyBlockItem item, SkyBlockPlayer player) {
        super(new NonPlayerItemUpdater(item.getItemStack()).getUpdatedItem().build());

        this.player = player;

        setAutoViewable(false);

        droppedItems.computeIfPresent(player, (key, value) -> {
            if (value.size() > 50) {
                value.getFirst().remove();
            }
            value.add(this);
            return value;
        });
        droppedItems.putIfAbsent(player, new ArrayList<>(List.of(this)));
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
