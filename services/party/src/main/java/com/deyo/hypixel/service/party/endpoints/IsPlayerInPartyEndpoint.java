package com.deyo.hypixel.service.party.endpoints;

import com.deyo.hypixel.commons.protocol.objects.party.IsPlayerInPartyProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.party.PartyCache;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class IsPlayerInPartyEndpoint implements RedisMessageHandler<
        IsPlayerInPartyProtocol.IsPlayerInPartyMessage,
        IsPlayerInPartyProtocol.IsPlayerInPartyResponse> {

    @Override
    public IsPlayerInPartyProtocol protocol() {
        return new IsPlayerInPartyProtocol();
    }

    @Override
    public IsPlayerInPartyProtocol.IsPlayerInPartyResponse handle(IsPlayerInPartyProtocol.IsPlayerInPartyMessage messageObject, RedisMessageContext context) {
        UUID playerUUID = messageObject.playerUUID();

        boolean isInParty = PartyCache.isInParty(playerUUID);

        return new IsPlayerInPartyProtocol.IsPlayerInPartyResponse(isInParty, true, null);
    }
}