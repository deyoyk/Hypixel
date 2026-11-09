package com.deyo.hypixel.pvp.entity.projectile;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityShootEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomEntityProjectileShootTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void shootDispatchesEventAndRemovesCancelledProjectile() {
        EventNode<Event> node = EventNode.all("projectile-shoot-test");
        AtomicBoolean fired = new AtomicBoolean();

        node.addListener(EntityShootEvent.class, event -> {
            fired.set(true);
            event.setCancelled(true);
        });
        MinecraftServer.getGlobalEventHandler().addChild(node);

        try {
            CustomEntityProjectile projectile = new CustomEntityProjectile(null, EntityType.ARROW);
            projectile.shootFromRotation(0, 0, 0, 1.0, 0, 0);

            assertTrue(fired.get());
            assertTrue(projectile.isRemoved());
        } finally {
            MinecraftServer.getGlobalEventHandler().removeChild(node);
        }
    }
}
