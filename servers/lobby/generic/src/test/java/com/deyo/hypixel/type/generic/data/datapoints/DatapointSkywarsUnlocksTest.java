package com.deyo.hypixel.type.generic.data.datapoints;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatapointSkywarsUnlocksTest {

    @Test
    void deepCloneDoesNotShareNestedModeCollections() {
        DatapointSkywarsUnlocks original = new DatapointSkywarsUnlocks("test");
        original.getValue().getSelectedPerksByMode().put("NORMAL", new ArrayList<>(List.of("perk_one")));
        original.getValue().getDisabledInsanePerks().put("INSANE", new HashSet<>(Set.of("perk_two")));

        DatapointSkywarsUnlocks clone = (DatapointSkywarsUnlocks) original.deepClone();
        clone.getValue().getSelectedPerksByMode().get("NORMAL").add("perk_three");
        clone.getValue().getDisabledInsanePerks().get("INSANE").add("perk_four");

        assertEquals(List.of("perk_one"), original.getValue().getSelectedPerksByMode().get("NORMAL"));
        assertEquals(Set.of("perk_two"), original.getValue().getDisabledInsanePerks().get("INSANE"));
    }
}
