package com.deyo.hypixel.type.replayviewer.redis.service;

import com.deyo.hypixel.commons.protocol.objects.game.ViewReplayPushProtocol;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TypedViewReplayHandlerTest {

    @Test
    void replayAndShareCodeAreConsumedOnce() {
        UUID uuid = UUID.randomUUID();
        TypedViewReplayHandler handler = new TypedViewReplayHandler();

        handler.handle(new ViewReplayPushProtocol.Request(uuid, "replay-1", "code-1"), null);

        assertEquals("replay-1", TypedViewReplayHandler.getAndRemoveReplay(uuid));
        assertNull(TypedViewReplayHandler.getAndRemoveReplay(uuid));
        assertEquals("code-1", TypedViewReplayHandler.getAndRemoveShareCode(uuid));
        assertNull(TypedViewReplayHandler.getAndRemoveShareCode(uuid));
    }

    @Test
    void missingShareCodeDoesNotClearReplay() {
        UUID uuid = UUID.randomUUID();
        TypedViewReplayHandler handler = new TypedViewReplayHandler();

        handler.handle(new ViewReplayPushProtocol.Request(uuid, "replay-2", null), null);

        assertNull(TypedViewReplayHandler.getAndRemoveShareCode(uuid));
        assertEquals("replay-2", TypedViewReplayHandler.getAndRemoveReplay(uuid));
    }
}
