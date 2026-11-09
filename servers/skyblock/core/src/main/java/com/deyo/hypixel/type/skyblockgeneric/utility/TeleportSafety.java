package com.deyo.hypixel.type.skyblockgeneric.utility;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.function.Function;

public final class TeleportSafety {
    private static final double STEP = 0.25;

    private TeleportSafety() {
    }

    public static boolean hasClearPath(Instance instance, Point fromFeet, Point toFeet) {
        return hasClearPath(instance::getBlock, fromFeet, toFeet);
    }

    static boolean hasClearPath(Function<Point, Block> blockLookup, Point fromFeet, Point toFeet) {
        return hasClearLine(blockLookup, fromFeet, toFeet)
                && hasClearLine(blockLookup, fromFeet.add(0, 1, 0), toFeet.add(0, 1, 0));
    }

    private static boolean hasClearLine(Function<Point, Block> blockLookup, Point from, Point to) {
        double dx = to.x() - from.x();
        double dy = to.y() - from.y();
        double dz = to.z() - from.z();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (distance < STEP) {
            return true;
        }

        int steps = (int) Math.ceil(distance / STEP);
        for (int step = 1; step <= steps; step++) {
            double progress = (double) step / steps;
            Point point = new Vec(
                    from.x() + dx * progress,
                    from.y() + dy * progress,
                    from.z() + dz * progress
            );
            if (!blockLookup.apply(point).air()) {
                return false;
            }
        }
        return true;
    }
}
