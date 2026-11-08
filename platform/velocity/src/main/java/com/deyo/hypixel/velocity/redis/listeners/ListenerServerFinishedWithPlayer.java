package com.deyo.hypixel.velocity.redis.listeners;

import com.velocitypowered.api.proxy.Player;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.FinishedWithPlayerProtocol;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.deyo.hypixel.velocity.gamemanager.TransferHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.Optional;
import java.util.UUID;

public class ListenerServerFinishedWithPlayer implements RedisMessageHandler<
        FinishedWithPlayerProtocol.Request,
        FinishedWithPlayerProtocol.Response> {

    @Override
    public RedisProtocol<FinishedWithPlayerProtocol.Request, FinishedWithPlayerProtocol.Response> protocol() {
        return new FinishedWithPlayerProtocol();
    }

    @Override
    public FinishedWithPlayerProtocol.Response handle(FinishedWithPlayerProtocol.Request message, RedisMessageContext context) {
        UUID playerUUID = UUID.fromString(message.uuid());

        Optional<Player> potentialPlayer = SkyBlockVelocity.getServer().getPlayer(playerUUID);
        if (potentialPlayer.isEmpty()) {
            return new FinishedWithPlayerProtocol.Response();
        }

        Player player = potentialPlayer.get();
        TransferHandler handler = new TransferHandler(player);
        if (!handler.isInLimbo()) {
            return new FinishedWithPlayerProtocol.Response();
        }
        handler.previousServerIsFinished();

        return new FinishedWithPlayerProtocol.Response();
    }
}
