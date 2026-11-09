package com.deyo.hypixel.type.skyblockgeneric.item;

import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurableSkyBlockItemCacheTest {

    @Test
    void concurrentRegistrationsRemainVisible() throws Exception {
        int count = 50;
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            List<Future<?>> registrations = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int id = i;
                registrations.add(executor.submit(() -> new ConfigurableSkyBlockItem(
                        "test_cache_" + id, Material.STONE, List.of(), new HashMap<>()).register()));
            }
            for (Future<?> registration : registrations) {
                registration.get();
            }

            for (int i = 0; i < count; i++) {
                assertNotNull(ConfigurableSkyBlockItem.getFromID("test_cache_" + i));
            }
        } finally {
            executor.shutdownNow();
        }
    }
}
