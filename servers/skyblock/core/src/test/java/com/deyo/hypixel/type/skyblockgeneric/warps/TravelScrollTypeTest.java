package com.deyo.hypixel.type.skyblockgeneric.warps;

import com.deyo.hypixel.commons.ServerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelScrollTypeTest {

    @Test
    void dwarvenScrollIsLinkedToTheDwarvenMinesIsland() {
        TravelScrollType scroll = TravelScrollType.getFromInternalName("dwarven");

        assertEquals(TravelScrollType.DWARVEN_MINES, scroll);
        assertEquals(TravelScrollIslands.DWARVEN_MINES, TravelScrollIslands.getFromTravelScroll(scroll));
        assertEquals(ServerType.SKYBLOCK_DWARVEN_MINES,
                TravelScrollIslands.getFromTravelScroll(scroll).getServerType());
        assertTrue(TravelScrollIslands.DWARVEN_MINES.getAssociatedScrolls().contains(scroll));
    }
}
