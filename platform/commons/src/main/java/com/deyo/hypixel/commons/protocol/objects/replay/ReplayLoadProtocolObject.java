package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayLoadProtocolObject extends RedisProtocol<
        ReplayLoadProtocolObject.LoadRequest,
        ReplayLoadProtocolObject.LoadResponse> {
    private static final Serializer<LoadRequest> SERIALIZER = new JacksonSerializer<>(LoadRequest.class);
    private static final Serializer<LoadResponse> RETURN_SERIALIZER = new JacksonSerializer<>(LoadResponse.class);

    @Override
    public Serializer<LoadRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<LoadResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record LoadRequest(UUID replayId) {}

    public record LoadResponse(
            boolean success,
            String errorMessage,
            ReplayMetadata metadata,
            List<DataChunk> dataChunks
    ) {}

    public record ReplayMetadata(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String serverId,
            String gameTypeName,
            String mapName,
            String mapHash,
            int version,
            long startTime,
            long endTime,
            int durationTicks,
            Map<UUID, String> players,
            Map<String, List<UUID>> teams,
            Map<String, TeamInfo> teamInfo,
            String winnerId,
            String winnerType,
            long dataSize,
            double mapCenterX,
            double mapCenterZ
    ) {}

    public record TeamInfo(String name, String colorCode, int color) {}

    public record DataChunk(
            int chunkIndex,
            int startTick,
            int endTick,
            byte[] compressedData
    ) {}
}
