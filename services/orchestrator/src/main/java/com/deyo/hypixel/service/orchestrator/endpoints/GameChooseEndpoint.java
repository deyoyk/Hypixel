package com.deyo.hypixel.service.orchestrator.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.GameInformationPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.ChooseGameProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GameChooseEndpoint implements RedisMessageHandler
		<ChooseGameProtocol.ChooseGameMessage,
				ChooseGameProtocol.ChooseGameResponse> {

	@Override
	public RedisProtocol<ChooseGameProtocol.ChooseGameMessage, ChooseGameProtocol.ChooseGameResponse> protocol() {
		return new ChooseGameProtocol();
	}

	@Override
	public ChooseGameProtocol.ChooseGameResponse handle(ChooseGameProtocol.ChooseGameMessage body, RedisMessageContext context) {
		RedisClient.requestServerFromService(body.server().uuid(), new GameInformationPushProtocol(),
				new GameInformationPushProtocol.Request(body.player(), body.gameId()));
		return new ChooseGameProtocol.ChooseGameResponse(true, null);
	}

}
