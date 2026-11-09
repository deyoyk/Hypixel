package com.deyo.hypixel.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class TestFlowOptions {
    private TestFlowOptions() {
    }

    static List<Map<String, Object>> parseServerConfigs(String serverConfigs) {
        if (serverConfigs == null || serverConfigs.isBlank()) return List.of();

        List<Map<String, Object>> configs = new ArrayList<>();
        for (String config : serverConfigs.split(",")) {
            String[] parts = config.trim().split(":");
            if (parts.length == 0 || parts[0].isBlank()) continue;

            String type = parts[0];
            int count = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            configs.add(Map.of("type", type, "count", count));
        }
        return configs;
    }
}
