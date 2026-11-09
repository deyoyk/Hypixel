package com.deyo.hypixel.type.replayviewer.redis.service;

import com.deyo.hypixel.commons.protocol.objects.game.ViewReplayPushProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TypedViewReplayHandler implements RedisMessageHandler<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> {
    private static final Map<UUID, String> replay = new ConcurrentHashMap<>();
    private static final Map<UUID, String> shareCode = new ConcurrentHashMap<>();

    private static final ViewReplayPushProtocol PROTOCOL = new ViewReplayPushProtocol();

    @Override
    public RedisProtocol<ViewReplayPushProtocol.Request, ViewReplayPushProtocol.Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public ViewReplayPushProtocol.Response handle(ViewReplayPushProtocol.Request request, RedisMessageContext context) {
        UUID uuid = request.uuid();
        String gameId = request.replayId();
        String code = request.shareCode();

        replay.put(uuid, gameId);
        if (code != null && !code.isEmpty()) {
            shareCode.put(uuid, code);
        }
        return new ViewReplayPushProtocol.Response(true);
    }

    @Nullable
    public static String getAndRemoveShareCode(UUID uuid) {
        return shareCode.remove(uuid);
    }

    @Nullable
    public static String getAndRemoveReplay(UUID uuid) {
        return replay.remove(uuid);
    }
}
