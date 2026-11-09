package com.deyo.hypixel.type.generic.data.datapoints;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatapointBedWarsHotbarTest {

    @Test
    void defaultHotbarIsImmutable() {
        assertThrows(UnsupportedOperationException.class,
                () -> DatapointBedWarsHotbar.defaultHotbar.put(
                        (byte) 1, DatapointBedWarsHotbar.HotbarItemType.BLOCKS));
    }

    @Test
    void missingOrInvalidSerializedHotbarReturnsIndependentDefaults() {
        Map<Byte, DatapointBedWarsHotbar.HotbarItemType> missing =
                DatapointBedWarsHotbar.serializer.deserialize(null);
        Map<Byte, DatapointBedWarsHotbar.HotbarItemType> invalid =
                DatapointBedWarsHotbar.serializer.deserialize("not-json");

        assertEquals(DatapointBedWarsHotbar.defaultHotbar, missing);
        assertEquals(DatapointBedWarsHotbar.defaultHotbar, invalid);

        missing.put((byte) 1, DatapointBedWarsHotbar.HotbarItemType.BLOCKS);
        assertFalse(DatapointBedWarsHotbar.defaultHotbar.containsKey((byte) 1));
        assertFalse(invalid.containsKey((byte) 1));
    }

    @Test
    void newDatapointStartsWithAnIndependentDefaultMap() {
        DatapointBedWarsHotbar datapoint = new DatapointBedWarsHotbar("test");
        datapoint.getValue().put((byte) 1, DatapointBedWarsHotbar.HotbarItemType.BLOCKS);

        assertFalse(DatapointBedWarsHotbar.defaultHotbar.containsKey((byte) 1));
    }
}
