package com.deyo.hypixel.type.skyblockgeneric.commands;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NBTCommandTest {

    @Test
    void clearCacheRemovesOnlyTheRequestedPlayer() {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        NBTCommand.itemNBTCache.put(first, "first");
        NBTCommand.itemNBTCache.put(second, "second");

        NBTCommand.clearCache(first);

        assertFalse(NBTCommand.itemNBTCache.containsKey(first));
        assertTrue(NBTCommand.itemNBTCache.containsKey(second));
        NBTCommand.clearCache(second);
    }
}
