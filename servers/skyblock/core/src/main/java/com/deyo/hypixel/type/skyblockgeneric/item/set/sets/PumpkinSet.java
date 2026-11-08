package com.deyo.hypixel.type.skyblockgeneric.item.set.sets;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.event.value.SkyBlockValueEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.value.ValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.set.impl.ArmorSet;

import java.util.ArrayList;
import java.util.List;

public class PumpkinSet extends SkyBlockValueEvent implements ArmorSet {
    @Override
    public String getName() {
        return "Pumpkin Buff";
    }

    @Override
    public ArrayList<String> getDescription() {
        return new ArrayList<>(List.of(
                "Reduces all taken damage by <a>+10%",
                "and deal &a+10% <7>more damage."
        ));
    }

    @Override
    public Class<? extends ValueUpdateEvent> getValueEvent() {
        return PlayerDamagedByMobValueUpdateEvent.class;
    }

    @Override
    public void run(ValueUpdateEvent event) {
        float value = (float) event.getValue();

        if (!isWearingSet(event.getPlayer())) return;
        int pieceCount = getWornPieceCount(event.getPlayer());

        event.setValue(value * 0.9);
    }

    @Override
    public ItemStatistics getStatistics() {
        return ItemStatistics.builder()
                .withAdditivePercentage(ItemStatistic.DAMAGE, 10D)
                .build();
    }
}
