package com.deyo.hypixel.commons.skyblock.item.reforge;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReforgeExpressionEvaluatorTest {

    @Test
    void respectsOperatorPrecedenceAndParentheses() {
        assertEquals(14, ReforgeExpressionEvaluator.evaluate("2 + 3 * 4", Map.of()), 1e-9);
        assertEquals(20, ReforgeExpressionEvaluator.evaluate("(2 + 3) * 4", Map.of()), 1e-9);
        assertEquals(-2, ReforgeExpressionEvaluator.evaluate("2 - 4", Map.of()), 1e-9);
    }

    @Test
    void substitutesVariables() {
        assertEquals(6, ReforgeExpressionEvaluator.evaluate("{level} * 2", Map.of("level", 3.0)), 1e-9);
        assertEquals(1, ReforgeExpressionEvaluator.evaluate("{missing} + 1", Map.of()), 1e-9);
    }

    @Test
    void invalidExpressionsReturnZero() {
        assertEquals(0, ReforgeExpressionEvaluator.evaluate("1 / 0", Map.of()), 1e-9);
    }
}
