package com.deyo.hypixel.service.party.endpoints;

import com.deyo.hypixel.commons.party.PartyAction;
import com.deyo.hypixel.commons.protocol.objects.party.SendPartyActionProtocol;
import com.deyo.hypixel.commons.protocol.objects.party.SendPartyActionProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.party.SendPartyActionProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.party.PartyCache;
import org.tinylog.Logger;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class PartyActionEndpoint implements RedisMessageHandler<Request, Response> {

    @Override
    public SendPartyActionProtocol protocol() {
        return new SendPartyActionProtocol();
    }

    @Override
    public Response handle(Request request, RedisMessageContext context) {
        try {
            PartyAction action = request.action();
            switch (action) {
                case PartyAction.Invite a -> PartyCache.handleInvite(a);
                case PartyAction.AcceptInvite a -> PartyCache.handleAcceptInvite(a);
                case PartyAction.Leave a -> PartyCache.handleLeave(a);
                case PartyAction.Disband a -> PartyCache.handleDisband(a);
                case PartyAction.Transfer a -> PartyCache.handleTransfer(a);
                case PartyAction.Kick a -> PartyCache.handleKick(a);
                case PartyAction.Promote a -> PartyCache.handlePromote(a);
                case PartyAction.Demote a -> PartyCache.handleDemote(a);
                case PartyAction.Warp a -> PartyCache.handleWarp(a);
                case PartyAction.Hijack a -> PartyCache.handleHijack(a);
                case PartyAction.Chat a -> PartyCache.handleChat(a);
                case PartyAction.SwitchedServer a -> PartyCache.handleSwitchedServer(a);
                case PartyAction.PlayerDisconnect a -> PartyCache.handlePlayerDisconnect(a);
                case PartyAction.PlayerRejoin a -> PartyCache.handlePlayerRejoin(a);
            }
            return Response.ok();
        } catch (Exception e) {
            Logger.error(e, "Failed to process party action");
            return Response.failure("Action processing failed: " + e.getMessage());
        }
    }
}
