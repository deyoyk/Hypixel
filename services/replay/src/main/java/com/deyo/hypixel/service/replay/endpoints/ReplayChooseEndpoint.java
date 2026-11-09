package com.deyo.hypixel.service.replay.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.ViewReplayPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.Map;
import java.util.UUID;

public class ReplayChooseEndpoint implements RedisMessageHandler
		<ChooseReplayProtocolObject.ChooseReplayMessage,
			ChooseReplayProtocolObject.ChooseReplayResponse> {

	@Override
	public RedisProtocol<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse> protocol() {
		return new ChooseReplayProtocolObject();
	}

	@Override
	public ChooseReplayProtocolObject.ChooseReplayResponse handle(ChooseReplayProtocolObject.ChooseReplayMessage body, RedisMessageContext context) {
		try {
			Map<UUID, ViewReplayPushProtocol.Response> responses = RedisClient.requestAllServersFromService(
				new ViewReplayPushProtocol(),
				new ViewReplayPushProtocol.Request(body.player(), body.replayId(), body.shareCode()),
				300).join();
			return new ChooseReplayProtocolObject.ChooseReplayResponse(!isHandled(responses));
		} catch (RuntimeException exception) {
			org.tinylog.Logger.error(exception, "Failed to route replay {} to a viewer", body.replayId());
			return new ChooseReplayProtocolObject.ChooseReplayResponse(true);
		}
	}

	static boolean isHandled(Map<UUID, ViewReplayPushProtocol.Response> responses) {
		for (ViewReplayPushProtocol.Response response : responses.values()) {
			if (response != null && response.success()) {
				return true;
			}
		}
		return false;
	}

}
