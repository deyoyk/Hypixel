package com.deyo.hypixel.type.generic.achievement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerAchievementHandlerTest {

    @Test
    void messageIsClickableWhenTheMenuIsSupported() {
        String message = PlayerAchievementHandler.achievementMessageFormat(true);

        assertTrue(message.contains("click:run:'/achievements'"), message);
    }

    @Test
    void messageIsPlainWhenTheMenuIsUnavailable() {
        String message = PlayerAchievementHandler.achievementMessageFormat(false);

        assertFalse(message.contains("click:"), message);
    }
}
