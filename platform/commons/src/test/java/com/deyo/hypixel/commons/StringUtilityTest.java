package com.deyo.hypixel.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilityTest {

    @Test
    void romanNumeralZeroIsEmpty() {
        assertEquals("", StringUtility.getAsRomanNumeral(0));
    }

    @Test
    void romanNumeralSingleDigits() {
        assertEquals("I", StringUtility.getAsRomanNumeral(1));
        assertEquals("IV", StringUtility.getAsRomanNumeral(4));
        assertEquals("V", StringUtility.getAsRomanNumeral(5));
        assertEquals("IX", StringUtility.getAsRomanNumeral(9));
    }

    @Test
    void romanNumeralCompound() {
        assertEquals("XLII", StringUtility.getAsRomanNumeral(42));
        assertEquals("MCMXCIX", StringUtility.getAsRomanNumeral(1999));
        assertEquals("MMXXVI", StringUtility.getAsRomanNumeral(2026));
    }

    @Test
    void capitalizeNormalCase() {
        assertEquals("Hello", StringUtility.capitalize("hello"));
        assertEquals("World", StringUtility.capitalize("WORLD"));
    }

    @Test
    void capitalizeEdgeCases() {
        assertEquals("", StringUtility.capitalize(""));
        assertNull(StringUtility.capitalize(null));
        assertEquals("A", StringUtility.capitalize("a"));
        assertEquals("A", StringUtility.capitalize("A"));
    }

    @Test
    void shortenNumberSmallValues() {
        assertEquals("0", StringUtility.shortenNumber(0));
        assertEquals("999", StringUtility.shortenNumber(999));
    }

    @Test
    void shortenNumberThousands() {
        assertEquals("1.0K", StringUtility.shortenNumber(1000));
        assertEquals("1.5K", StringUtility.shortenNumber(1500));
        assertEquals("999.9K", StringUtility.shortenNumber(999_900));
    }

    @Test
    void shortenNumberMillionsAndBillions() {
        assertEquals("1.0M", StringUtility.shortenNumber(1_000_000));
        assertEquals("2.5M", StringUtility.shortenNumber(2_500_000));
        assertEquals("1.0B", StringUtility.shortenNumber(1_000_000_000));
    }

    @Test
    void commaifyAddsThousandsSeparators() {
        assertEquals("1,234", StringUtility.commaify(1234));
        assertEquals("1,000,000", StringUtility.commaify(1_000_000));
        assertEquals("0", StringUtility.commaify(0));
    }

    @Test
    void roundToPrecision() {
        assertEquals(3.14, StringUtility.roundTo(3.14159, 2), 1e-9);
        assertEquals(3.142, StringUtility.roundTo(3.14159, 3), 1e-9);
        assertEquals(3.0, StringUtility.roundTo(3.14159, 0), 1e-9);
    }

    @Test
    void formatTimeLeftBoundaries() {
        assertEquals("0s", StringUtility.formatTimeLeft(0));
        assertEquals("59s", StringUtility.formatTimeLeft(59_000));
        assertEquals("1m", StringUtility.formatTimeLeft(60_000));
        assertEquals("1m 30s", StringUtility.formatTimeLeft(90_000));
        assertEquals("1h", StringUtility.formatTimeLeft(3_600_000));
        assertEquals("1d", StringUtility.formatTimeLeft(86_400_000));
    }

    @Test
    void stripColorRemovesSectionCodes() {
        assertEquals("Hello", StringUtility.stripColor("§aHello"));
        assertEquals("Plain", StringUtility.stripColor("Plain"));
        assertEquals("Mixed text", StringUtility.stripColor("§cMixed §btext"));
    }

    @Test
    void commaifyAndThUsesOrdinalSuffixes() {
        assertEquals("1st", StringUtility.commaifyAndTh(1));
        assertEquals("2nd", StringUtility.commaifyAndTh(2));
        assertEquals("3rd", StringUtility.commaifyAndTh(3));
        assertEquals("4th", StringUtility.commaifyAndTh(4));
        assertEquals("11th", StringUtility.commaifyAndTh(11));
        assertEquals("12th", StringUtility.commaifyAndTh(12));
        assertEquals("13th", StringUtility.commaifyAndTh(13));
        assertEquals("21st", StringUtility.commaifyAndTh(21));
        assertEquals("1,022nd", StringUtility.commaifyAndTh(1022));
    }

    @Test
    void decimalifyRoundsUpWithoutAllocatingPerCall() {
        assertEquals("1.24", StringUtility.decimalify(1.231, 2));
        assertEquals("1.3", StringUtility.decimalify(1.21, 1));
        assertEquals("0.01", StringUtility.decimalify(0.001, 2));
    }

    @Test
    void parseDurationSupportsCombinedUnits() {
        assertEquals(0, StringUtility.parseDuration(""));
        assertEquals(1000, StringUtility.parseDuration("1s"));
        assertEquals(90_000, StringUtility.parseDuration("1m30s"));
        assertEquals(90_061_000, StringUtility.parseDuration("1d1h1m1s"));
        assertEquals(0, StringUtility.parseDuration("nonsense"));
    }

    @Test
    void formatAsDateProducesAReadableDate() {
        long epochMillis = 1_704_067_200_000L;
        String formatted = StringUtility.formatAsDate(epochMillis);
        assertTrue(formatted.matches("[A-Z][a-z]+ \\d{2}, \\d{4}"), formatted);
    }

    @Test
    void auctionTimeDropsSpacesAndTrailingSecondsMarker() {
        assertEquals("1m30", StringUtility.getAuctionSetupFormattedTime(90_000));
        assertEquals("1m", StringUtility.getAuctionSetupFormattedTime(60_000));
        assertEquals("45", StringUtility.getAuctionSetupFormattedTime(45_000));
    }

    @Test
    void toNormalCaseReplacesUnderscoresAndNormalizesCasing() {
        assertEquals("Skyblock", StringUtility.toNormalCase("skyblock"));
        assertEquals("Sky Block", StringUtility.toNormalCase("SKY_BLOCK"));
        assertEquals("S", StringUtility.toNormalCase("s"));
    }

    @Test
    void commaifyIsSafeUnderConcurrentUse() throws Exception {
        int threads = 8;
        int iterations = 2_000;
        Thread[] workers = new Thread[threads];
        boolean[] failed = new boolean[1];

        for (int i = 0; i < threads; i++) {
            workers[i] = Thread.ofPlatform().start(() -> {
                for (int iteration = 0; iteration < iterations; iteration++) {
                    String expected = iteration % 2 == 0 ? "1,234,567" : "0";
                    String actual = StringUtility.commaify(iteration % 2 == 0 ? 1_234_567 : 0);
                    if (!expected.equals(actual)) {
                        failed[0] = true;
                        return;
                    }
                }
            });
        }

        for (Thread worker : workers) {
            worker.join();
        }

        assertFalse(failed[0], "commaify produced corrupted output under concurrency");
    }

    @Test
    void formatSecondsPadsSeconds() {
        assertEquals("0:00", StringUtility.formatSeconds(0));
        assertEquals("0:05", StringUtility.formatSeconds(5));
        assertEquals("1:05", StringUtility.formatSeconds(65));
        assertEquals("10:00", StringUtility.formatSeconds(600));
    }

    @Test
    void formatTicksConvertsTicksToMinutesAndSeconds() {
        assertEquals("0:00", StringUtility.formatTicks(0));
        assertEquals("0:05", StringUtility.formatTicks(100));
        assertEquals("1:05", StringUtility.formatTicks(1300));
    }
}
