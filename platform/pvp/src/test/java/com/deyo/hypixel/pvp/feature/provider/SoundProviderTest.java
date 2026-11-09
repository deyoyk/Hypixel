package com.deyo.hypixel.pvp.feature.provider;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SoundProviderTest {

    @Test
    void defaultProviderForwardsEmitterCoordinates() {
        Sound sound = Sound.sound(Key.key("test:ping"), Sound.Source.PLAYER, 1.0f, 1.0f);
        AtomicReference<Object[]> captured = new AtomicReference<>();
        Audience audience = (Audience) Proxy.newProxyInstance(
                Audience.class.getClassLoader(),
                new Class<?>[]{Audience.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("playSound") && args != null && args.length == 4) {
                        captured.set(args);
                    }
                    return null;
                });

        SoundProvider.DEFAULT.playSound(audience, sound, 1.5, 64.0, -3.5);

        Object[] call = captured.get();
        assertSame(sound, call[0]);
        assertEquals(1.5, (double) call[1]);
        assertEquals(64.0, (double) call[2]);
        assertEquals(-3.5, (double) call[3]);
    }
}
