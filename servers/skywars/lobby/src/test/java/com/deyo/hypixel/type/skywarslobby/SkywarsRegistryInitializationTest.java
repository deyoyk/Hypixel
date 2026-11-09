package com.deyo.hypixel.type.skywarslobby;

import net.minestom.server.MinecraftServer;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKitRegistry;
import com.deyo.hypixel.type.skywarslobby.perk.SkywarsPerkRegistry;
import com.deyo.hypixel.type.skywarslobby.soulwell.SoulWellUpgradeRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkywarsRegistryInitializationTest {

    private static final Path CONFIG_DIR = Path.of("configuration/skywars");
    private static boolean configExisted;

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        configExisted = Files.exists(CONFIG_DIR);
    }

    @AfterAll
    static void cleanUpConfig() throws IOException {
        if (configExisted || !Files.exists(CONFIG_DIR)) return;
        try (Stream<Path> paths = Files.walk(CONFIG_DIR)) {
            paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException ignored) {
                }
            });
        }
    }

    @BeforeEach
    void resetRegistries() throws Exception {
        reset(SkywarsKitRegistry.class, "initialized", "KITS");
        reset(SkywarsPerkRegistry.class, "initialized", "PERKS");
        reset(SoulWellUpgradeRegistry.class, "initialized", "upgrades");
    }

    @Test
    void initializeIsIdempotent() {
        SkywarsKitRegistry.initialize();
        int kits = SkywarsKitRegistry.getAllKits().size();
        SkywarsKitRegistry.initialize();
        assertEquals(kits, SkywarsKitRegistry.getAllKits().size());

        SkywarsPerkRegistry.initialize();
        int perks = SkywarsPerkRegistry.getAllPerks().size();
        SkywarsPerkRegistry.initialize();
        assertEquals(perks, SkywarsPerkRegistry.getAllPerks().size());

        SoulWellUpgradeRegistry.initialize();
        int upgrades = SoulWellUpgradeRegistry.getAllUpgrades().size();
        SoulWellUpgradeRegistry.initialize();
        assertEquals(upgrades, SoulWellUpgradeRegistry.getAllUpgrades().size());
    }

    @Test
    void lazyInitializationIsSafeUnderConcurrentReads() throws Exception {
        int threads = 8;
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int index = 0; index < threads; index++) {
            Thread.startVirtualThread(() -> {
                try {
                    start.await();
                    for (int iteration = 0; iteration < 25; iteration++) {
                        assertFalse(SkywarsKitRegistry.getAllKits().isEmpty());
                        assertFalse(SkywarsPerkRegistry.getAllPerks().isEmpty());
                        assertFalse(SoulWellUpgradeRegistry.getAllUpgrades().isEmpty());
                    }
                } catch (Throwable throwable) {
                    failure.compareAndSet(null, throwable);
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        assertTrue(done.await(20, TimeUnit.SECONDS));
        assertNull(failure.get());
    }

    @SuppressWarnings("unchecked")
    private static void reset(Class<?> registry, String initializedField, String mapField) throws Exception {
        Field initialized = registry.getDeclaredField(initializedField);
        initialized.setAccessible(true);
        initialized.setBoolean(null, false);

        Field map = registry.getDeclaredField(mapField);
        map.setAccessible(true);
        ((Map<?, ?>) map.get(null)).clear();
    }
}
