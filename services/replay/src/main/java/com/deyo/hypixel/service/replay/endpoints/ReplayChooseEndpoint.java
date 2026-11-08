package com.deyo.hypixel.service.replay.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.ViewReplayPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.replay.ChooseReplayProtocolObject;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

public class ReplayChooseEndpoint implements RedisMessageHandler
		<ChooseReplayProtocolObject.ChooseReplayMessage,
			ChooseReplayProtocolObject.ChooseReplayResponse> {

	@Override
	public RedisProtocol<ChooseReplayProtocolObject.ChooseReplayMessage, ChooseReplayProtocolObject.ChooseReplayResponse> protocol() {
		return new ChooseReplayProtocolObject();
	}

	@Override
	public ChooseReplayProtocolObject.ChooseReplayResponse handle(ChooseReplayProtocolObject.ChooseReplayMessage body, RedisMessageContext context) {
		RedisClient.requestAllServersFromService(
			new ViewReplayPushProtocol(),
			new ViewReplayPushProtocol.Request(body.player(), body.replayId(), body.shareCode()),
			300);
		return new ChooseReplayProtocolObject.ChooseReplayResponse(false);
	}

}
