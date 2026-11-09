package com.deyo.hypixel.type.generic.entity.npc;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NPCMovementControllerTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void emptyPathCompletesImmediately() {
        NPCMovementController controller = controller();

        assertTrue(controller.walkPath(List.of()).isDone());
    }

    @Test
    void unspawnedEntityFailsTheMovement() {
        NPCMovementController controller = controller();

        var movement = controller.walkPath(List.of(new Pos(0, 0, 0)));

        assertTrue(movement.isCompletedExceptionally());
        assertFalse(controller.isMoving());
    }

    private static NPCMovementController controller() {
        return new NPCMovementController(new EntityCreature(EntityType.ZOMBIE));
    }
}
