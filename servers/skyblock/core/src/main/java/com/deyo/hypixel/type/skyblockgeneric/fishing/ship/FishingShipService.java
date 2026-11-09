package com.deyo.hypixel.type.skyblockgeneric.fishing.ship;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointShipState;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingShipPartComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingShipService {
    private static final int MAX_SHIP_NAME_LENGTH = 32;

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

    public static boolean renameShip(SkyBlockPlayer player, String name) {
        DatapointShipState.ShipState state = getState(player);
        if (!applyShipName(state, name)) return false;
        player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.SHIP_STATE, DatapointShipState.class).setValue(state);
        return true;
    }

    static boolean applyShipName(DatapointShipState.ShipState state, String name) {
        String normalized = normalizeShipName(name);
        if (normalized == null) return false;
        state.setShipName(normalized);
        return true;
    }

    static String normalizeShipName(String name) {
        if (name == null) return null;
        String normalized = StringUtility.stripColor(name).trim();
        return normalized.isEmpty() || normalized.length() > MAX_SHIP_NAME_LENGTH ? null : normalized;
    }
}
