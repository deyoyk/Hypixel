package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatapointDefaultValueTest {

    @Test
    void shopAndMissionDatapointsStartWithUsableDefaults() {
        assertNotNull(new DatapointShopData("test").getValue());
        assertNotNull(new DatapointMissionData("test").getValue());
    }
}
