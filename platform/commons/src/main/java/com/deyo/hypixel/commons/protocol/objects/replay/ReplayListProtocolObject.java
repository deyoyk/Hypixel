package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayListProtocolObject extends RedisProtocol<
        ReplayListProtocolObject.ListRequest,
        ReplayListProtocolObject.ListResponse> {
    private static final Serializer<ListRequest> SERIALIZER = new JacksonSerializer<>(ListRequest.class);
    private static final Serializer<ListResponse> RETURN_SERIALIZER = new JacksonSerializer<>(ListResponse.class);

    @Override
    public Serializer<ListRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<ListResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record ListRequest(UUID playerId, int limit, ServerType filterType) {}

    public record ListResponse(boolean success, List<ReplaySummary> replays) {}

    public record ReplaySummary(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String serverId,
            String gameTypeName,
            String mapName,
            long startTime,
            long endTime,
            int durationTicks,
            int playerCount,
            Map<UUID, String> players,
            String winnerId,
            String winnerType,
            long dataSize
    ) {}
}
