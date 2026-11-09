package com.deyo.hypixel.dungeons;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CatacombsDungeon extends GameDungeon {
    @Override
    public int getRoomCount() {
        return rooms.size();
    }

    private Map<Map.Entry<Integer, Integer>, DungeonRoom> rooms = new HashMap<>();
    private Set<DungeonDoor> doors = new LinkedHashSet<>();

    public CatacombsDungeon setRoom(int x, int y, DungeonRoom room) {
        rooms.put(Map.entry(x, y), room);
        return this;
    }

    public DungeonRoom getRoom(int x, int y) {
        return rooms.get(Map.entry(x, y));
    }

    public Map<Map.Entry<Integer, Integer>, DungeonRoom> getRooms() {
        return Map.copyOf(rooms);
    }

    public List<DungeonDoor> getDoorConnections() {
        return List.copyOf(doors);
    }

    public boolean isConnected(int x1, int y1, int x2, int y2) {
        return doors.contains(normalizedDoor(x1, y1, x2, y2));
    }

    public List<int[]> getDoors(int x, int y) {
        final List<int[]> neighbours = new ArrayList<>(4);
        if (isConnected(x, y, x - 1, y)) {
            neighbours.add(new int[]{x - 1, y});
        }
        if (isConnected(x, y, x + 1, y)) {
            neighbours.add(new int[]{x + 1, y});
        }
        if (isConnected(x, y, x, y - 1)) {
            neighbours.add(new int[]{x, y - 1});
        }
        if (isConnected(x, y, x, y + 1)) {
            neighbours.add(new int[]{x, y + 1});
        }
        return neighbours;
    }

    public void connectRooms(int x1, int y1, int x2, int y2) {
        DungeonRoom room1 = getRoom(x1, y1);
        DungeonRoom room2 = getRoom(x2, y2);
        if (room1 == null || room2 == null) {
            throw new IllegalArgumentException("Room at " + x1 + ", " + y1 + " or " + x2 + ", " + y2 + " does not exist");
        }

        doors.add(normalizedDoor(x1, y1, x2, y2));
    }

    private static DungeonDoor normalizedDoor(int x1, int y1, int x2, int y2) {
        if (x1 < x2 || (x1 == x2 && y1 <= y2)) {
            return new DungeonDoor(x1, y1, x2, y2);
        }
        return new DungeonDoor(x2, y2, x1, y1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Loop over the dungeon rooms, center them and print them

        int width = rooms.keySet().stream().mapToInt(Map.Entry::getKey).max().orElse(0);
        int height = rooms.keySet().stream().mapToInt(Map.Entry::getValue).max().orElse(0);

        for (int y = 0; y <= height; y++) {
            for (int x = 0; x <= width; x++) {
                DungeonRoom room = rooms.get(Map.entry(x, y));
                if (room == null) {
                    builder.append(DungeonUtilities.center(" ", 30, ' '));
                } else {
                    builder.append(DungeonUtilities.center(
                            (isConnected(x, y, x - 1, y) ? "|" : " ") +
                                    room + (isConnected(x, y, x + 1, y) ? "|" : " "),
                            30, ' '));
                }
            }
            builder.append("\n");

            if (y < height) {
                for (int x = 0; x <= width; x++) {
                    DungeonRoom room = rooms.get(Map.entry(x, y));
                    if (room == null) {
                        builder.append(DungeonUtilities.center(" ", 30, ' '));
                    } else {
                        builder.append(DungeonUtilities.center(
                                (isConnected(x, y, x, y + 1) ? "--" : "   "),
                                30, ' '));
                    }
                }
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    @Getter
    @Setter
    public static class DungeonRoom {
        private DungeonRoomType roomType;
        private boolean isCritical;
        private int stage = 0;
        private int corridorNumber = 0;

        public DungeonRoom(DungeonRoomType roomType) {
            this.roomType = roomType;
        }

        @Override
        public String toString() {
            return roomType.name() + (isCritical ? " (C)" : "") + (stage > 0 ? " (S-" + stage + ")" : "")
                    + (corridorNumber > 0 ? " (C-" + corridorNumber + ")" : "");
        }

        public static DungeonRoom ofBase() {
            return new DungeonRoom(DungeonRoomType.BASE);
        }
    }

    public record DungeonDoor(int x1, int y1, int x2, int y2) { }
}
