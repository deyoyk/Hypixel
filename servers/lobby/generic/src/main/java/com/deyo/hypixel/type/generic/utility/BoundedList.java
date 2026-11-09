package com.deyo.hypixel.type.generic.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class BoundedList<T> {
    private final int limit;
    private final Predicate<T> removed;
    private final Consumer<T> remove;
    private final List<T> items = new ArrayList<>();

    public BoundedList(int limit, Predicate<T> removed, Consumer<T> remove) {
        this.limit = Math.max(1, limit);
        this.removed = removed;
        this.remove = remove;
    }

    public synchronized void add(T item) {
        items.removeIf(removed);
        while (items.size() >= limit) {
            remove.accept(items.removeFirst());
        }
        items.add(item);
    }

    public synchronized List<T> snapshot() {
        return List.copyOf(items);
    }

    public synchronized void clear() {
        items.clear();
    }

    public synchronized int size() {
        return items.size();
    }
}
