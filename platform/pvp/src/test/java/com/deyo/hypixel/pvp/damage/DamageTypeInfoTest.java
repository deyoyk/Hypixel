package com.deyo.hypixel.pvp.damage;

import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.registry.RegistryKey;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DamageTypeInfoTest {

    @Test
    @SuppressWarnings("unchecked")
    void everyDamageTypeHasExplicitInfo() throws ReflectiveOperationException {
        Class<?> damageTypes = Class.forName("net.minestom.server.entity.damage.DamageTypes");

        for (Field field : damageTypes.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != RegistryKey.class) {
                continue;
            }

            field.setAccessible(true);
            RegistryKey<DamageType> key = (RegistryKey<DamageType>) field.get(null);
            assertTrue(DamageTypeInfo.INFO_MAP.containsKey(key),
                    () -> "Missing DamageTypeInfo for " + field.getName());
        }
    }

    @Test
    void fireProjectileAndExplosionFlagsArePreserved() {
        assertTrue(DamageTypeInfo.of(DamageType.CAMPFIRE).fire());
        assertTrue(DamageTypeInfo.of(DamageType.WIND_CHARGE).projectile());
        assertTrue(DamageTypeInfo.of(DamageType.BAD_RESPAWN_POINT).explosive());
        assertTrue(DamageTypeInfo.of(DamageType.FALL).fall());
        assertTrue(DamageTypeInfo.of(DamageType.THORNS).thorns());
    }
}
