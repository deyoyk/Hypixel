package com.deyo.hypixel.type.generic.utility;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundedListTest {

    @Test
    void evictsOldestEntriesAtTheLimit() {
        List<Integer> evicted = new ArrayList<>();
        BoundedList<Integer> list = new BoundedList<>(3, value -> false, evicted::add);

        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        assertEquals(List.of(2, 3, 4), list.snapshot());
        assertEquals(List.of(0, 1), evicted);
    }

    @Test
    void purgesRemovedEntriesBeforeCounting() {
        Set<Integer> removed = new HashSet<>();
        BoundedList<Integer> list = new BoundedList<>(2, removed::contains, value -> {
        });
        list.add(1);
        list.add(2);
        removed.add(1);

        list.add(3);

        assertEquals(List.of(2, 3), list.snapshot());
    }

    @Test
    void clearDropsAllEntries() {
        BoundedList<String> list = new BoundedList<>(4, value -> false, value -> {
        });
        list.add("first");
        list.add("second");

        list.clear();

        assertTrue(list.snapshot().isEmpty());
        assertEquals(0, list.size());
    }
}
