package com.deyo.hypixel.service.replay.endpoints;

import com.deyo.hypixel.commons.protocol.objects.game.ViewReplayPushProtocol;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayChooseEndpointTest {

    @Test
    void handledWhenAtLeastOneServerAcceptedTheReplay() {
        assertTrue(ReplayChooseEndpoint.isHandled(Map.of(
                UUID.randomUUID(), new ViewReplayPushProtocol.Response(false),
                UUID.randomUUID(), new ViewReplayPushProtocol.Response(true)
        )));
    }

    @Test
    void notHandledWhenNoServerAcceptedTheReplay() {
        assertFalse(ReplayChooseEndpoint.isHandled(Map.of(
                UUID.randomUUID(), new ViewReplayPushProtocol.Response(false)
        )));
        assertFalse(ReplayChooseEndpoint.isHandled(Map.of()));
    }
}
