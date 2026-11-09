package com.deyo.hypixel.commons.text;

import com.deyo.hypixel.commons.ChatUtility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextTest {

    @Test
    void plainAndPixelWidthUseTheRenderedComponent() {
        Text text = Text.of("<c>Hello");

        assertEquals("Hello", text.plain());
        assertEquals(ChatUtility.FontInfo.getLength("Hello"), text.pixelWidth());
    }

    @Test
    void linesSplitOnNewlines() {
        assertEquals(2, Text.literal("a\nb").lines().size());
        assertEquals("a", Text.literal("a\nb").lines().getFirst().plain());
    }

    @Test
    void centeredTextAddsLeadingSpaces() {
        String centered = Text.component(TextLayout.center(Text.literal("Hello").asComponent())).plain();
        assertTrue(centered.startsWith(" "));
        assertTrue(centered.endsWith("Hello"));
    }
}
