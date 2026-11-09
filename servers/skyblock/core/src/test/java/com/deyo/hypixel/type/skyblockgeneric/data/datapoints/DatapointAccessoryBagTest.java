package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatapointAccessoryBagTest {

    @Test
    void discoveredAccessoriesRoundTrip() throws Exception {
        DatapointAccessoryBag datapoint = new DatapointAccessoryBag("test");
        datapoint.getValue().addDiscoveredAccessory(ItemType.COBBLESTONE);

        DatapointAccessoryBag.PlayerAccessoryBag restored =
                datapoint.getSerializer().deserialize(datapoint.getSerializedValue());

        assertTrue(restored.hasDiscoveredAccessory(ItemType.COBBLESTONE));
    }

    @Test
    void legacyStringDiscoveredAccessoriesLoad() {
        DatapointAccessoryBag.PlayerAccessoryBag restored = new DatapointAccessoryBag("test")
                .getSerializer()
                .deserialize("{\"discoveredAccessories\":\"[\\\"COBBLESTONE\\\"]\"}");

        assertTrue(restored.hasDiscoveredAccessory(ItemType.COBBLESTONE));
    }
}
