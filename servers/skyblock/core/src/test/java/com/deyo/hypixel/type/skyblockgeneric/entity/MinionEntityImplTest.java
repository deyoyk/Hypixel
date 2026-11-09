package com.deyo.hypixel.type.skyblockgeneric.entity;

import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MinionEntityImplTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void activeMinionsSupportLockFreeIteration() {
        assertInstanceOf(CopyOnWriteArrayList.class, MinionEntityImpl.getActiveMinions());
    }
}
