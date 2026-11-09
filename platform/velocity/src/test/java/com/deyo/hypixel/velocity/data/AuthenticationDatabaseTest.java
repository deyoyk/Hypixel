package com.deyo.hypixel.velocity.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationDatabaseTest {

    @Test
    void passwordVerificationUsesTheStoredSalt() {
        AuthenticationDatabase.AuthenticationData data =
                AuthenticationDatabase.makeFromPassword("correct horse battery staple");

        assertTrue(data.matches("correct horse battery staple"));
        assertFalse(data.matches("wrong password"));
    }

    @Test
    void eachRegistrationUsesAFreshSalt() {
        AuthenticationDatabase.AuthenticationData first = AuthenticationDatabase.makeFromPassword("password");
        AuthenticationDatabase.AuthenticationData second = AuthenticationDatabase.makeFromPassword("password");

        assertNotEquals(first.salt(), second.salt());
        assertNotEquals(first.hash(), second.hash());
    }
}
