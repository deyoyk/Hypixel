package com.deyo.hypixel.type.skyblockgeneric.fishing.ship;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointShipState;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingShipPartComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingShipService {

    public static DatapointShipState.ShipState getState(SkyBlockPlayer player) {
        return player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).getValue();
    }

    public static void installPart(SkyBlockPlayer player, String itemId, FishingShipPartComponent definition) {
        DatapointShipState.ShipState state = getState(player);
        switch (definition.getSlot()) {
            case HELM -> state.setHelm(itemId);
            case ENGINE -> state.setEngine(itemId);
            case HULL -> state.setHull(itemId);
        }
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }

    public static void unlockDestination(SkyBlockPlayer player, String destinationId) {
        DatapointShipState.ShipState state = getState(player);
        state.unlockDestination(destinationId);
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
    }
}
