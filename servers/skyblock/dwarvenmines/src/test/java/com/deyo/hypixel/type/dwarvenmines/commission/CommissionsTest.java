package com.deyo.hypixel.type.dwarvenmines.commission;

import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointCommissions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommissionsTest {

    @Test
    void extraSlotAddsAMissingCommissionWithoutDuplicatingNames() {
        DatapointCommissions.PlayerCommissionData data = new DatapointCommissions.PlayerCommissionData();
        data.setTotalCompleted(100);
        data.getActiveCommissions().add(new DatapointCommissions.ActiveCommission(Commissions.CONSISTENT.get(0).name));
        data.getActiveCommissions().add(new DatapointCommissions.ActiveCommission(Commissions.CONSISTENT.get(1).name));

        assertTrue(Commissions.addCommissionSlot(data));
        assertEquals(3, data.getActiveCommissions().size());
        assertEquals(3, data.getActiveCommissions().stream()
                .map(DatapointCommissions.ActiveCommission::getCommissionName)
                .distinct()
                .count());
    }

    @Test
    void extraSlotIsIgnoredWhenTheCapacityIsReached() {
        DatapointCommissions.PlayerCommissionData data = new DatapointCommissions.PlayerCommissionData();
        data.setTotalCompleted(5);
        data.getActiveCommissions().add(new DatapointCommissions.ActiveCommission(Commissions.CONSISTENT.get(0).name));
        data.getActiveCommissions().add(new DatapointCommissions.ActiveCommission(Commissions.CONSISTENT.get(1).name));

        assertFalse(Commissions.addCommissionSlot(data));
        assertEquals(2, data.getActiveCommissions().size());
    }
}
