package com.deyo.hypixel.type.skyblockgeneric.utility;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeleportSafetyTest {

    private static final Vec FROM = new Vec(0.5, 64, 0.5);
    private static final Vec TO = new Vec(4.5, 64, 0.5);

    @Test
    void clearTwoHighPathIsAllowed() {
        assertTrue(TeleportSafety.hasClearPath(blockLookup((x, y) -> false), FROM, TO));
    }

    @Test
    void blockInFeetLanePreventsTeleport() {
        assertFalse(TeleportSafety.hasClearPath(blockLookup((x, y) -> x == 2 && y == 64), FROM, TO));
    }

    @Test
    void blockInHeadLanePreventsTeleport() {
        assertFalse(TeleportSafety.hasClearPath(blockLookup((x, y) -> x == 2 && y == 65), FROM, TO));
    }

    private static Function<Point, Block> blockLookup(BiPredicate<Integer, Integer> blocked) {
        return point -> blocked.test(point.blockX(), point.blockY()) ? Block.STONE : Block.AIR;
    }
}
