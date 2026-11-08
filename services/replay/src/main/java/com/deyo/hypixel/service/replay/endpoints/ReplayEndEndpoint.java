package com.deyo.hypixel.service.replay.endpoints;

import com.deyo.hypixel.commons.protocol.objects.replay.ReplayEndProtocolObject;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.service.replay.ReplayService;
import com.deyo.hypixel.service.replay.session.ReplaySessionManager;
import org.tinylog.Logger;

import java.util.concurrent.TimeUnit;

public class ReplayEndEndpoint implements RedisMessageHandler<
        ReplayEndProtocolObject.EndMessage,
        ReplayEndProtocolObject.EndResponse> {

    @Override
    public ReplayEndProtocolObject protocol() {
        return new ReplayEndProtocolObject();
    }

    @Override
    public ReplayEndProtocolObject.EndResponse handle(ReplayEndProtocolObject.EndMessage msg, RedisMessageContext context) {

        try {
            Logger.info("Ending replay session {} (duration: {} ticks)", msg.replayId(), msg.durationTicks());
            ReplaySessionManager.EndResult result = ReplayService.getSessionManager()
                    .endSession(
                            msg.replayId(),
                            msg.endTime(),
                            msg.durationTicks()
                    )
                    .get(30, TimeUnit.SECONDS); // Wait up to 30 seconds

            Logger.info("Replay {} finalized: {} -> {} bytes",
                    msg.replayId(), result.totalBytes(), result.compressedBytes());

            return new ReplayEndProtocolObject.EndResponse(
                    result.success(),
                    result.totalBytes(),
                    result.compressedBytes(),
                    result.success()
            );

        } catch (Exception e) {
            Logger.error(e, "Failed to end replay session");
            return new ReplayEndProtocolObject.EndResponse(false, 0, 0, false);
        }
    }
}
