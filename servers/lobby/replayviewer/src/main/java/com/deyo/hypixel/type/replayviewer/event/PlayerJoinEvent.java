package com.deyo.hypixel.type.replayviewer.event;

import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.replay.ReplayLoadProtocolObject;
import com.deyo.hypixel.commons.protocol.objects.replay.ReplayMapLoadProtocolObject;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.game.replay.ReplayError;
import com.deyo.hypixel.type.game.replay.ReplayMetadata;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;
import com.deyo.hypixel.type.replayviewer.TypeReplayViewerLoader;
import com.deyo.hypixel.type.replayviewer.playback.MapDeserializer;
import com.deyo.hypixel.type.replayviewer.playback.ReplayData;
import com.deyo.hypixel.type.replayviewer.playback.ReplaySession;
import com.deyo.hypixel.type.replayviewer.redis.service.TypedViewReplayHandler;
import com.deyo.hypixel.type.replayviewer.util.ReplayShareCodec;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayerJoinEvent implements HypixelEventClass {
    private static final ProxyService REPLAY_SERVICE = new ProxyService(ServiceType.REPLAY);
    private static final ExecutorService LOAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

    @SneakyThrows
    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(AsyncPlayerConfigurationEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        tryGame(player, false, event);
    }

    private void tryGame(HypixelPlayer player, boolean isRetry, AsyncPlayerConfigurationEvent event) {
        String replayStr = TypedViewReplayHandler.getAndRemoveReplay(player.getUuid());
        if (replayStr == null) {
            if (!isRetry) {
                ScheduleUtility.delay(() -> tryGame(player, true, event), 20);
                return;
            }
            event.setSpawningInstance(HypixelConst.getEmptyInstance());
            player.sendTo(ServerType.PROTOTYPE_LOBBY);
            return;
        }

        UUID replayId;
        try {
            replayId = UUID.fromString(replayStr);
        } catch (IllegalArgumentException e) {
            event.setSpawningInstance(HypixelConst.getEmptyInstance());
            player.sendTo(ServerType.PROTOTYPE_LOBBY);
            return;
        }

        var existingSession = TypeReplayViewerLoader.getSessionByReplayId(replayId);
        if (existingSession.isPresent()) {
            ReplaySession session = existingSession.get();
            event.setSpawningInstance(session.getInstance());

            Pos spawnPos = new Pos(session.getMetadata().getMapCenterX(), 100, session.getMetadata().getMapCenterZ());
            event.getPlayer().setRespawnPoint(spawnPos);

            CompletableFuture.runAsync(() -> {
                ScheduleUtility.delay(() -> {
                    session.addViewer(player);
                    player.teleport(spawnPos);
                    player.sendMessage("<a>Joined existing replay session.");
                }, 20);
            });
            return;
        }

        InstanceContainer instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        instance.setChunkSupplier(LightingChunk::new);

        event.setSpawningInstance(instance);
        event.getPlayer().setRespawnPoint(new Pos(0, 100, 0));

        CompletableFuture.supplyAsync(() -> fetchReplay(player, replayId), LOAD_EXECUTOR)
            .thenAccept(bundle -> {
                if (bundle == null) return;
                ScheduleUtility.delay(() -> finishReplay(player, bundle, instance), 20);
            })
            .exceptionally(exception -> {
                Logger.error(exception, "Failed to load replay {}", replayId);
                ScheduleUtility.delay(() -> player.sendMessage("<c>Failed to load the replay."), 1);
                return null;
            });
    }

    private record ReplayBundle(
            ReplayMetadata metadata,
            ReplayData replayData,
            MapDeserializer.DecodedMap decodedMap,
            String shareCode,
            List<String> messages) {
    }

    @SneakyThrows
    private ReplayBundle fetchReplay(HypixelPlayer player, UUID replayId) {
        String shareCode = TypedViewReplayHandler.getAndRemoveShareCode(player.getUuid());
        List<String> messages = new ArrayList<>();

        ReplayLoadProtocolObject.LoadResponse response = REPLAY_SERVICE
            .<ReplayLoadProtocolObject.LoadRequest, ReplayLoadProtocolObject.LoadResponse>handleRequest(
                new ReplayLoadProtocolObject.LoadRequest(replayId))
            .join();

        if (!response.success() || response.metadata() == null) {
            return null;
        }

        ReplayLoadProtocolObject.ReplayMetadata protoMetadata = response.metadata();
        Map<String, ReplayMetadata.TeamInfo> teamInfo = new HashMap<>();
        protoMetadata.teamInfo().forEach((teamId, info) ->
            teamInfo.put(teamId, new ReplayMetadata.TeamInfo(info.name(), info.colorCode(), info.color())));

        ReplayMetadata metadata = ReplayMetadata.builder()
            .replayId(protoMetadata.replayId())
            .gameId(protoMetadata.gameId())
            .serverType(protoMetadata.serverType())
            .serverId(protoMetadata.serverId())
            .gameTypeName(protoMetadata.gameTypeName())
            .mapName(protoMetadata.mapName())
            .mapHash(protoMetadata.mapHash())
            .version(protoMetadata.version())
            .startTime(protoMetadata.startTime())
            .endTime(protoMetadata.endTime())
            .durationTicks(protoMetadata.durationTicks())
            .players(protoMetadata.players())
            .teams(protoMetadata.teams())
            .teamInfo(teamInfo)
            .winnerId(protoMetadata.winnerId())
            .dataSize(protoMetadata.dataSize())
            .mapCenterX(protoMetadata.mapCenterX())
            .mapCenterZ(protoMetadata.mapCenterZ())
            .build();

        ReplayData replayData = new ReplayData();
        if (response.dataChunks() != null && !response.dataChunks().isEmpty()) {
            ReplayData.IntegrityReport integrityReport = replayData.loadFromProtocolChunks(
                response.dataChunks(),
                metadata.getDurationTicks()
            );
            if (integrityReport.hasIssues()) {
                messages.add("<c>Some moments may be missing. " + ReplayError.REPLAY_INCOMPLETE.format());
            }
        }

        MapDeserializer.DecodedMap decodedMap = null;
        if (metadata.getMapHash() != null && !metadata.getMapHash().isEmpty()) {
            ReplayMapLoadProtocolObject.MapLoadResponse mapResponse = REPLAY_SERVICE
                .<ReplayMapLoadProtocolObject.MapLoadRequest, ReplayMapLoadProtocolObject.MapLoadResponse>handleRequest(
                    new ReplayMapLoadProtocolObject.MapLoadRequest(metadata.getMapHash()))
                .join();

            if (!mapResponse.success() || !mapResponse.found()) {
                messages.add("<e>Map data not available, using empty world.");
            } else if (mapResponse.compressedData() != null && mapResponse.compressedData().length > 0) {
                decodedMap = MapDeserializer.decode(mapResponse.compressedData());
            }
        }

        return new ReplayBundle(metadata, replayData, decodedMap, shareCode, messages);
    }

    private void finishReplay(HypixelPlayer player, ReplayBundle bundle, InstanceContainer instance) {
        try {
            if (bundle.decodedMap() != null) {
                bundle.decodedMap().apply(instance);
            }
            for (String message : bundle.messages()) {
                player.sendMessage(message);
            }

            ReplayMetadata metadata = bundle.metadata();
            Pos spawnPos;
            int startTick = 0;

            if (bundle.shareCode() != null) {
                ReplayShareCodec.ShareData shareData = ReplayShareCodec.decode(
                    bundle.shareCode(),
                    metadata.getMapCenterX(),
                    metadata.getMapCenterZ()
                );
                if (shareData != null) {
                    spawnPos = shareData.position();
                    startTick = Math.min(shareData.tick(), metadata.getDurationTicks() - 1);
                    player.sendMessage("<a>Restored shared replay position");
                } else {
                    spawnPos = new Pos(metadata.getMapCenterX(), 100, metadata.getMapCenterZ());
                    player.sendMessage("<e>Invalid share code, using default position");
                }
            } else {
                spawnPos = new Pos(metadata.getMapCenterX(), 100, metadata.getMapCenterZ());
            }

            player.teleport(spawnPos);

            ReplaySession session = new ReplaySession(player, metadata, instance, bundle.replayData());
            TypeReplayViewerLoader.registerSession(player.getUuid(), session);

            if (startTick > 0) {
                session.seekTo(startTick);
            }

            session.play();
        } catch (Exception exception) {
            Logger.error(exception, "Failed to finish loading replay {}", bundle.metadata().getReplayId());
            player.sendMessage("<c>Failed to load the replay.");
        }
    }
}
