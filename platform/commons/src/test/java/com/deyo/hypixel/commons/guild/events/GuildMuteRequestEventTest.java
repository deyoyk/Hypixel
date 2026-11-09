package com.deyo.hypixel.commons.guild.events;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GuildMuteRequestEventTest {

    @Test
    void storesAUuidTargetAndUsesNullForEveryone() {
        UUID muter = UUID.randomUUID();
        UUID target = UUID.randomUUID();

        GuildMuteRequestEvent single = new GuildMuteRequestEvent(muter, target, 1000L);
        assertEquals(target, single.getTarget());
        assertEquals(1000L, single.getDuration());

        GuildMuteRequestEvent everyone = new GuildMuteRequestEvent(muter, null, 2000L);
        assertNull(everyone.getTarget());
    }
}
