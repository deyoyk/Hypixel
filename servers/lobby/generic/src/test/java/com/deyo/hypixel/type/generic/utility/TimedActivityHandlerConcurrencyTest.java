package com.deyo.hypixel.type.generic.utility;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TimedActivityHandlerConcurrencyTest {

    @Test
    void activeHandlersUseAConcurrentMap() throws ReflectiveOperationException {
        Field field = TimedActivityHandler.class.getDeclaredField("ACTIVE_HANDLERS");
        field.setAccessible(true);

        assertTrue(field.get(null) instanceof ConcurrentMap);
    }
}
