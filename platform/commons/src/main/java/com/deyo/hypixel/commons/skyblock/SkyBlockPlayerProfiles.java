package com.deyo.hypixel.commons.skyblock;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class SkyBlockPlayerProfiles {
    private final static String[] PROFILE_NAMES = {
            "Zucchini", "Papaya", "Watermelon", "Pineapple",
            "Lemon", "Apple", "Banana", "Orange", "Pear",
            "Coconuts", "Cherry", "Strawberry", "Raspberry",
            "Kiwi", "Mango", "Pomegranate", "Grape",
            "Avocado", "Tomato", "Cucumber", "Carrot",
            "Potato", "Onion", "Garlic", "Celery",
            "Broccoli", "Cauliflower", "Spinach", "Asparagus"
    };
    private static final Map<UUID, SkyBlockPlayerProfiles> profilesCache = new ConcurrentHashMap<>();

    UUID currentlySelected = null;
    ArrayList<UUID> profiles = new ArrayList<>();

    public SkyBlockPlayerProfiles(UUID playerUuid) {
        profilesCache.put(playerUuid, this);
    }

    public SkyBlockPlayerProfiles() {
    }

    public void addProfile(UUID profile) {
        profiles.add(profile);
    }

    public void removeProfile(UUID profile) {
        profiles.remove(profile);
    }

    public Map<String, Object> serialize() {
        return Map.of(
                "selected", currentlySelected != null ? currentlySelected.toString() : "",
                "profiles", profiles.stream().map(UUID::toString).toList()
        );
    }

    public static SkyBlockPlayerProfiles deserialize(Map<String, Object> map) {
        SkyBlockPlayerProfiles skyBlockPlayerProfiles = new SkyBlockPlayerProfiles(UUID.fromString((String) map.get("_id"))); // Player UUID
        Object selected = map.get("selected");
        if (selected instanceof String string && !string.isEmpty()) {
            skyBlockPlayerProfiles.currentlySelected = UUID.fromString(string); // Profile UUID
        }
        skyBlockPlayerProfiles.profiles = new ArrayList<>();
        Object rawProfiles = map.get("profiles");
        if (rawProfiles instanceof List<?> storedProfiles) {
            for (Object profile : storedProfiles) {
                skyBlockPlayerProfiles.profiles.add(UUID.fromString(profile.toString()));
            }
        }
        return skyBlockPlayerProfiles;
    }

    public static void unload(UUID playerUuid) {
        profilesCache.remove(playerUuid);
    }

    public static String getRandomName() {
        return PROFILE_NAMES[java.util.concurrent.ThreadLocalRandom.current().nextInt(PROFILE_NAMES.length)];
    }

    public static SkyBlockPlayerProfiles get(UUID uuid) {
        return profilesCache.get(uuid);
    }
}
