package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReplayStartProtocolObject extends RedisProtocol<
        ReplayStartProtocolObject.StartMessage,
        ReplayStartProtocolObject.StartResponse> {

    @Override
    public Serializer<StartMessage> getSerializer() {
        return new JacksonSerializer<>(StartMessage.class);
    }

    @Override
    public Serializer<StartResponse> getReturnSerializer() {
        return new JacksonSerializer<>(StartResponse.class);
    }

    public record StartMessage(
            UUID replayId,
            String gameId,
            ServerType serverType,
            String serverId,
            String gameTypeName,
            String mapName,
            String mapHash,
            long startTime,
            double mapCenterX,
            double mapCenterZ,
            Map<UUID, String> players,
            Map<String, List<UUID>> teams,
            Map<String, TeamInfo> teamInfo
    ) {}

    public record StartResponse(boolean success, String message) {}

    public record TeamInfo(String name, String colorCode, int color) {}
}
