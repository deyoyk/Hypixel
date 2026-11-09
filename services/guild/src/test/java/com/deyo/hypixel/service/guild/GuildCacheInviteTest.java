package com.deyo.hypixel.service.guild;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildCacheInviteTest {

    @Test
    void claimInviteValidatesTheInviterWithoutConsumingTheInvite() {
        UUID guildId = UUID.randomUUID();
        UUID inviter = UUID.randomUUID();
        UUID accepter = UUID.randomUUID();
        Map<UUID, GuildCache.PendingGuildInvite> invites = new ConcurrentHashMap<>();
        GuildCache.PendingGuildInvite invite =
                new GuildCache.PendingGuildInvite(guildId, inviter, accepter);
        invites.put(accepter, invite);

        assertNull(GuildCache.claimInvite(invites, accepter, UUID.randomUUID()));
        assertTrue(invites.containsKey(accepter));

        assertSame(invite, GuildCache.claimInvite(invites, accepter, inviter));
        assertFalse(invites.containsKey(accepter));
        assertNull(GuildCache.claimInvite(invites, accepter, inviter));
    }
}
