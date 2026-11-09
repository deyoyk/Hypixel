package com.deyo.hypixel.type.replayviewer.entity;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityPose;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayPlayerEntityTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void deathStateTogglesPoseAndIsIdempotent() {
        ReplayPlayerEntity entity = new ReplayPlayerEntity(
                "Tester", null, null, UUID.randomUUID(), 7);

        assertFalse(entity.isDead());
        assertEquals(EntityPose.STANDING, entity.getPose());

        entity.setDead(true);
        assertTrue(entity.isDead());
        assertEquals(EntityPose.DYING, entity.getPose());

        entity.setDead(true);
        assertTrue(entity.isDead());

        entity.setDead(false);
        assertFalse(entity.isDead());
        assertEquals(EntityPose.STANDING, entity.getPose());
    }
}
