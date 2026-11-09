package com.deyo.hypixel.type.skyblockgeneric.region;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkyBlockRegionTest {

    @Test
    void boundsAndVolumeAreDerivedFromBothCorners() {
        SkyBlockRegion region = new SkyBlockRegion("test",
                new Pos(9, 4, 9),
                new Pos(0, 0, 0),
                RegionType.VILLAGE,
                null);

        assertEquals(500, region.getVolume());
        assertTrue(region.insideRegion(new Pos(5, 2, 5)));
        assertFalse(region.insideRegion(new Pos(-1, 2, 5)));
        assertFalse(region.insideRegion(new Pos(5, 5, 5)));
    }

    @Test
    void movingACornerInvalidatesTheCachedVolume() {
        SkyBlockRegion region = new SkyBlockRegion("test",
                new Pos(0, 0, 0),
                new Pos(1, 1, 1),
                RegionType.VILLAGE,
                null);

        assertEquals(8, region.getVolume());
        region.setSecondLocation(new Pos(2, 2, 2));
        assertEquals(27, region.getVolume());
    }

    @Test
    void movingACornerInvalidatesTheCachedBounds() {
        SkyBlockRegion region = new SkyBlockRegion("test",
                new Pos(0, 0, 0),
                new Pos(1, 1, 1),
                RegionType.VILLAGE,
                null);

        assertTrue(region.insideRegion(new Pos(1, 1, 1)));
        assertFalse(region.insideRegion(new Pos(2, 2, 2)));

        region.setSecondLocation(new Pos(2, 2, 2));

        assertTrue(region.insideRegion(new Pos(2, 2, 2)));
        assertEquals(List.of(0, 2, 0, 2, 0, 2), region.getBounds());
    }

    @Test
    void randomPositionsIncludeTheMaximumCorner() {
        SkyBlockRegion region = new SkyBlockRegion("test",
                new Pos(0, 0, 0),
                new Pos(1, 1, 1),
                RegionType.VILLAGE,
                null);

        boolean sawMaximum = false;
        for (int attempt = 0; attempt < 1000 && !sawMaximum; attempt++) {
            Pos position = region.getRandomPosition();
            assertTrue(position.x() >= 0 && position.x() <= 1);
            assertTrue(position.y() >= 0 && position.y() <= 1);
            assertTrue(position.z() >= 0 && position.z() <= 1);
            sawMaximum = position.x() == 1 && position.y() == 1 && position.z() == 1;
        }

        assertTrue(sawMaximum);
    }
}
