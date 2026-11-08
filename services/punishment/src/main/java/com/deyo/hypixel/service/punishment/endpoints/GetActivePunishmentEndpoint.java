package com.deyo.hypixel.service.punishment.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.punishment.GetActivePunishmentProtocol;
import com.deyo.hypixel.commons.punishment.ActivePunishment;
import com.deyo.hypixel.commons.punishment.PunishmentRedis;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;
import java.util.Optional;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetActivePunishmentEndpoint implements RedisMessageHandler
        <GetActivePunishmentProtocol.GetActivePunishmentMessage,
                GetActivePunishmentProtocol.GetActivePunishmentResponse> {

    @Override
    public RedisProtocol<GetActivePunishmentProtocol.GetActivePunishmentMessage, GetActivePunishmentProtocol.GetActivePunishmentResponse> protocol() {
        return new GetActivePunishmentProtocol();
    }

    @Override
    public GetActivePunishmentProtocol.GetActivePunishmentResponse handle(GetActivePunishmentProtocol.GetActivePunishmentMessage messageObject, RedisMessageContext context) {
        Optional<ActivePunishment> existing = PunishmentRedis.getActive(messageObject.target(), messageObject.type());
        if (existing.isEmpty()) {
            return new GetActivePunishmentProtocol.GetActivePunishmentResponse(false, null, null, null, 0, List.of(), true, null);
        }

        ActivePunishment punishment = existing.get();
        return new GetActivePunishmentProtocol.GetActivePunishmentResponse(
                true,
                punishment.type(),
                punishment.banId(),
                punishment.reason(),
                punishment.expiresAt(),
                punishment.tags(),
                true,
                null
        );
    }
}
