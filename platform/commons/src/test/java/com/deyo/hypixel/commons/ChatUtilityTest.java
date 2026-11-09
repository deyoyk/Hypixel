package com.deyo.hypixel.commons;

import com.deyo.hypixel.commons.ChatUtility.FontInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChatUtilityTest {

    @Test
    void fontLookupIsCaseSensitiveAndMatchesEnumWidths() {
        assertSame(FontInfo.A, FontInfo.getDefaultFontInfo('A'));
        assertSame(FontInfo.a, FontInfo.getDefaultFontInfo('a'));
        assertSame(FontInfo.I, FontInfo.getDefaultFontInfo('I'));
        assertSame(FontInfo.i, FontInfo.getDefaultFontInfo('i'));
        assertSame(FontInfo.SPACE, FontInfo.getDefaultFontInfo(' '));
        assertSame(FontInfo.DOUBLE_LEFT_ARROW, FontInfo.getDefaultFontInfo('«'));
    }

    @Test
    void unknownCharactersUseTheDefaultWidth() {
        assertSame(FontInfo.DEFAULT, FontInfo.getDefaultFontInfo('\u4e2d'));
        assertEquals(4, FontInfo.getDefaultFontInfo('\u4e2d').getLength());
    }

    @Test
    void stripTokensRemovesEachCodePair() {
        assertEquals("Hello", FontInfo.stripTokens("§aHello"));
        assertEquals("Hello", FontInfo.stripTokens("He§cl§alo"));
        assertEquals("", FontInfo.stripTokens("§a§b"));
        assertEquals("§", FontInfo.stripTokens("§"));
    }

    @Test
    void getLengthIgnoresFormattingCodes() {
        assertEquals(FontInfo.getLength("Hello"), FontInfo.getLength("§aHello"));
        assertEquals(FontInfo.getLength("Hello"), FontInfo.getLength("§aHello§r"));
    }

    @Test
    void boldCodesIncreaseMeasuredWidthUntilReset() {
        int plain = FontInfo.getLength("A");
        int bold = FontInfo.getLength("§lA");
        int reset = FontInfo.getLength("§lA§rA");

        assertTrue(bold > plain);
        assertEquals(bold + plain, reset);
    }

    @Test
    void wrapMatchesTheOriginalReferenceAlgorithm() {
        String[] messages = {
                "",
                "one two three",
                "§lBOLD §rnormal words here",
                "short §cword §lverylongwordthatcannotfitanywhere",
                "§a§lAll bold and colored text with several words",
                "trailing   spaces   here ",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa b c d",
                "§r§l§m§n§o§k formatted codes only"
        };
        int[] widths = {20, 40, 80, 240};

        for (String message : messages) {
            for (int width : widths) {
                assertEquals(referenceWrap(message, width), FontInfo.wrap(message, width),
                        "message=" + message + " width=" + width);
            }
        }
    }

    @Test
    void wrapHandlesUnicodeAndLongSingleWords() {
        assertEquals(List.of("«hello»"), FontInfo.wrap("«hello»", 240));
        assertEquals(List.of("aaaa", "bbbb"), FontInfo.wrap("aaaa bbbb", 10));
        assertEquals(List.of("§lAAAAAAAA"), FontInfo.wrap("§lAAAAAAAA", 5));
    }

    private static List<String> referenceWrap(String message, int maxPxWidth) {
        List<String> lines = new ArrayList<>();
        if (message == null || message.isEmpty()) {
            return lines;
        }

        StringBuilder currentLine = new StringBuilder();
        for (String word : message.split(" ")) {
            String candidate = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (referenceLength(candidate) <= maxPxWidth) {
                currentLine = new StringBuilder(candidate);
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine.toString());
                }
                currentLine = new StringBuilder(word);
            }
        }
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    private static int referenceLength(String message) {
        double messagePxSize = 0;
        boolean previousCode = false;
        boolean bold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
            } else if (previousCode) {
                previousCode = false;
                bold = bold ? c != 'r' : c == 'l';
            } else {
                FontInfo fontInfo = FontInfo.getDefaultFontInfo(c);
                messagePxSize += bold ? fontInfo.getBoldLength() + 1.5 : fontInfo.getLength() + 1;
            }
        }
        return (int) messagePxSize;
    }
}
