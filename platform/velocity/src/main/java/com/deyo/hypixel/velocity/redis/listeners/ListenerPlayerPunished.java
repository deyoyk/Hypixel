package com.deyo.hypixel.velocity.redis.listeners;

import com.google.gson.Gson;
import io.sentry.Sentry;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.PunishPlayerProtocol;
import com.deyo.hypixel.commons.punishment.PunishmentReason;
import com.deyo.hypixel.commons.punishment.PunishmentTag;
import com.deyo.hypixel.commons.punishment.ActivePunishment;
import com.deyo.hypixel.commons.punishment.PunishmentMessages;
import com.deyo.hypixel.commons.punishment.PunishmentType;
import com.deyo.hypixel.commons.punishment.template.BanType;
import com.deyo.hypixel.commons.punishment.template.MuteType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.deyo.hypixel.velocity.text.ProxyText;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;

public class ListenerPlayerPunished implements RedisMessageHandler<
        PunishPlayerProtocol.Request,
        PunishPlayerProtocol.Response> {

    @Override
    public RedisProtocol<PunishPlayerProtocol.Request, PunishPlayerProtocol.Response> protocol() {
        return new PunishPlayerProtocol();
    }

    @Override
    public PunishPlayerProtocol.Response handle(PunishPlayerProtocol.Request message, RedisMessageContext context) {
        UUID target = UUID.fromString(message.target());
        String type = message.type();
        String id = message.id();
        long expiresAt = message.expiresAt();

        PunishmentReason reason;
        try {
            String banString = message.reasonBan();
            String muteString = message.reasonMute();

            if (banString != null) {
                reason = new PunishmentReason(BanType.valueOf(banString));
            } else if (muteString != null) {
                reason = new PunishmentReason(MuteType.valueOf(muteString));
            } else {
                throw new IllegalArgumentException("Missing reason ban or reason_mute");
            }
        } catch (IllegalArgumentException e) {
            Logger.error("Failed to parse punishment reason from message: " + message, e);
            Sentry.captureException(e);
            return new PunishPlayerProtocol.Response();
        }

        List<PunishmentTag> tags = List.of();
        if (message.tags() != null) {
            try {
                tags = List.of(new Gson().fromJson(message.tags(), PunishmentTag[].class));
            } catch (Exception ignored) {
            }
        }

        PunishmentType punishmentType = PunishmentType.valueOf(type);
        PunishmentReason finalReason = reason;
        List<PunishmentTag> finalTags = tags;
        SkyBlockVelocity.getServer().getPlayer(target).ifPresent((player) -> {
            ActivePunishment activePunishment = new ActivePunishment(type, id, finalReason, expiresAt, finalTags);
            switch (punishmentType) {
                case BAN -> ProxyText.disconnect(player, PunishmentMessages.banMessage(activePunishment));
                case MUTE -> player.sendMessage(PunishmentMessages.muteMessage(activePunishment));
                case WARNING -> player.sendMessage(Text.of(
                        "<c>[WARNING] <7>You have received a warning for the following reason: <e>{}",
                        finalReason.getReasonString()));
                default -> {}
            }
        });

        return new PunishPlayerProtocol.Response();
    }
}
