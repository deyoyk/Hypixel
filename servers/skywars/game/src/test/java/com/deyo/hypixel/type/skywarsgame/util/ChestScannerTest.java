package com.deyo.hypixel.type.skywarsgame.util;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.palette.Palette;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChestScannerTest {

    @Test
    void emptyAndNonChestPalettesAreSkipped() {
        assertFalse(ChestScanner.paletteContainsChest(Palette.blocks()));

        Palette stone = Palette.blocks();
        stone.set(0, 0, 0, Block.STONE.stateId());
        assertFalse(ChestScanner.paletteContainsChest(stone));
    }

    @Test
    void chestAndTrappedChestPalettesAreDetected() {
        Palette chest = Palette.blocks();
        chest.set(0, 0, 0, Block.CHEST.stateId());
        assertTrue(ChestScanner.paletteContainsChest(chest));

        Palette trapped = Palette.blocks();
        trapped.set(0, 0, 0, Block.TRAPPED_CHEST.stateId());
        assertTrue(ChestScanner.paletteContainsChest(trapped));
    }
}
