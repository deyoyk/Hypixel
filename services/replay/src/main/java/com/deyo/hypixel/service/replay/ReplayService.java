package com.deyo.hypixel.service.replay;

import lombok.Getter;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.replay.session.ReplaySessionManager;
import com.deyo.hypixel.service.replay.storage.ReplayDatabase;
import org.tinylog.Logger;

import java.util.List;

public class ReplayService implements SkyBlockService {

    @Getter
	private static ReplayDatabase database;
    @Getter
	private static ReplaySessionManager sessionManager;

    static void main() {
        Logger.info("Starting Replay Service...");

        String mongoUri = ConfigProvider.settings().getMongodb();
        database = new ReplayDatabase();
        database.connect(mongoUri);

        sessionManager = new ReplaySessionManager(database);
        sessionManager.startCleanupTask();

        SkyBlockService.init(new ReplayService());

        Logger.info("Replay Service started successfully");
    }

	@Override
    public ServiceType getType() {
        return ServiceType.REPLAY;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.replay.endpoints", RedisMessageHandler.class).toList();
    }
}
