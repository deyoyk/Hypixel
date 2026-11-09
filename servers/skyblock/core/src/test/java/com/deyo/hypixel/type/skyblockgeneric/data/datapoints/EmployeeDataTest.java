package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeDataTest {

    @Test
    void productionScalesWithLevel() {
        DatapointChocolateFactory.EmployeeData employee =
                new DatapointChocolateFactory.EmployeeData(5, 10.5);

        assertEquals(5, employee.level());
        assertEquals(10.5, employee.baseProduction());
        assertEquals(52.5, employee.getProductionPerSecond(), 1e-9);
    }
}
