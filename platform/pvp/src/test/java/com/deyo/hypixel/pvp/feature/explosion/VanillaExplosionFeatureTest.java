package com.deyo.hypixel.pvp.feature.explosion;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.pvp.events.ExplosivePrimeEvent;
import com.deyo.hypixel.pvp.feature.config.FeatureConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VanillaExplosionFeatureTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void cancelledPrimeEventReportsFailure() {
        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.eventNode().addListener(ExplosivePrimeEvent.class, event -> event.setCancelled(true));

        VanillaExplosionFeature feature = new VanillaExplosionFeature(new FeatureConfiguration());
        feature.initDependencies();

        boolean primed = feature.primeExplosive(instance, new Pos(0, 0, 0),
                new ExplosionFeature.IgnitionCause.Explosion(null), 80);

        assertFalse(primed);
    }

    @Test
    void noOpFeatureNeverPrimes() {
        assertFalse(ExplosionFeature.NO_OP.primeExplosive(
                MinecraftServer.getInstanceManager().createInstanceContainer(),
                new Pos(0, 0, 0),
                new ExplosionFeature.IgnitionCause.Explosion(null),
                80));
    }
}
