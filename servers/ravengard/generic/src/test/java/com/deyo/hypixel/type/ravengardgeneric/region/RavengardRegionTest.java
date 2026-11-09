package com.deyo.hypixel.type.ravengardgeneric.region;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RavengardRegionTest {

    @Test
    void boundsAndVolumeAreDerivedFromBothCorners() {
        RavengardRegion region = new RavengardRegion("test",
                new Pos(9, 4, 9),
                new Pos(0, 0, 0),
                RavengardRegionType.RAVENPORT,
                null);

        assertEquals(500, region.getVolume());
        assertTrue(region.insideRegion(new Pos(5, 2, 5)));
        assertFalse(region.insideRegion(new Pos(-1, 2, 5)));
        assertFalse(region.insideRegion(new Pos(5, 5, 5)));
    }

    @Test
    void movingACornerInvalidatesTheCachedVolume() {
        RavengardRegion region = new RavengardRegion("test",
                new Pos(0, 0, 0),
                new Pos(1, 1, 1),
                RavengardRegionType.RAVENPORT,
                null);

        assertEquals(8, region.getVolume());
        region.setSecondLocation(new Pos(2, 2, 2));
        assertEquals(27, region.getVolume());
    }
}
