package com.deyo.hypixel.commons.skyblock.item.attribute;

import lombok.Getter;
import lombok.Setter;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Setter
@Getter
public abstract class ItemAttribute<T> {
    private static volatile List<Constructor<? extends ItemAttribute>> constructors = List.of();

    public T value;

    public ItemAttribute() {
        value = getDefaultValue(null);
    }

    public abstract String getKey();

    public abstract T getDefaultValue(@Nullable ItemStatistics defaultStatistics);

    public abstract T loadFromString(String string);

    public abstract String saveIntoString();

    public static void registerItemAttributes() {
        List<Constructor<? extends ItemAttribute>> discovered = new ArrayList<>();
        loopThroughPackage(
                "com.deyo.hypixel.commons.skyblock.item.attribute.attributes", ItemAttribute.class
        ).forEach(attribute -> {
            try {
                discovered.add(attribute.getClass().getDeclaredConstructor());
            } catch (NoSuchMethodException exception) {
                throw new RuntimeException(exception);
            }
        });
        constructors = List.copyOf(discovered);
    }

    public static Collection<ItemAttribute> getPossibleAttributes() {
        List<Constructor<? extends ItemAttribute>> current = constructors;
        List<ItemAttribute> result = new ArrayList<>(current.size());
        for (Constructor<? extends ItemAttribute> constructor : current) {
            try {
                result.add(constructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                throw new RuntimeException(exception);
            }
        }
        return result;
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
