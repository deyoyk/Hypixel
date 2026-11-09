package com.deyo.hypixel.type.skyblockgeneric.item.set;

import com.deyo.hypixel.type.skyblockgeneric.item.set.impl.ArmorSet;
import com.deyo.hypixel.type.skyblockgeneric.item.set.sets.LapisArmorSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ArmorSetRegistryTest {

    @Test
    void armorSetInstancesAreReused() {
        ArmorSet first = ArmorSetRegistry.LAPIS.getArmorSetInstance();

        assertNotNull(first);
        assertInstanceOf(LapisArmorSet.class, first);
        assertSame(first, ArmorSetRegistry.LAPIS.getArmorSetInstance());
    }

    @Test
    void parkArmorHasNoImplementationInstance() {
        assertNull(ArmorSetRegistry.PARK_ARMOR.getArmorSetInstance());
    }
}
