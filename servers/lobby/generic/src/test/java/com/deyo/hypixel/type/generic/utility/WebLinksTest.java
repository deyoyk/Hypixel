package com.deyo.hypixel.type.generic.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WebLinksTest {

    @Test
    void storeLinkIsClickableAndPointsAtTheStore() {
        assertLink(WebLinks.storeLink(), WebLinks.STORE);
    }

    @Test
    void forumsLinkIsClickable() {
        assertLink(WebLinks.forumsLink(), WebLinks.FORUMS);
    }

    @Test
    void guildsLinkIsClickable() {
        assertLink(WebLinks.guildsLink(), WebLinks.GUILDS);
    }

    @Test
    void dailyRewardLinkIsClickable() {
        assertLink(WebLinks.dailyRewardLink(), WebLinks.DAILY_REWARDS);
    }

    @Test
    void socialMediaRewardsLinkIsClickable() {
        assertLink(WebLinks.socialMediaRewardsLink(), WebLinks.SOCIAL_MEDIA_REWARDS);
    }

    private static void assertLink(com.deyo.hypixel.commons.text.Text link, String url) {
        String serialized = link.serialize();
        assertTrue(serialized.contains("click:url"), serialized);
        assertTrue(serialized.contains(url), serialized);
    }
}
