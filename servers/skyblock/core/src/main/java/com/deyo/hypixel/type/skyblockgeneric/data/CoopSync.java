package com.deyo.hypixel.type.skyblockgeneric.data;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import com.deyo.hypixel.libs.LinkedField;
import com.deyo.hypixel.commons.data.DeyoData;
import com.deyo.hypixel.commons.skyblock.CoopLinks;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

public final class CoopSync {

    private CoopSync() {}

    public static void subscribe() {
        for (SkyBlockDataHandler.Data data : SkyBlockDataHandler.Data.values()) {
            if (data.coopField() == null) continue;
            DeyoData.profile().subscribe(data.coopField(),
                    (coopId, oldValue, newValue, affected) -> apply(data, coopId, newValue));
        }
    }

    public static <T> void track(LinkedField<UUID, T> field) {
        DeyoData.profile().subscribe(field, (coopId, oldValue, newValue, affected) -> {});
    }

    private static void apply(SkyBlockDataHandler.Data data, UUID coopId, String newValue) {
        if (newValue == null) return;
        for (SkyBlockPlayer player : SkyBlockGenericLoader.getLoadedPlayers()) {
            SkyBlockDataHandler handler = player.getSkyblockDataHandler();
            if (handler == null) continue;

            UUID profileId = handler.getCurrentProfileId();
            UUID playerCoopId = DeyoData.profile().getLinkKey(profileId, CoopLinks.COOP).orElse(profileId);
            if (!playerCoopId.equals(coopId)) continue;

            Datapoint<?> datapoint = handler.getDatapoint(data.getKey());
            if (datapoint == null) continue;
            datapoint.deserializeValue(newValue);

            Player online = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
            if (online != null && data.onChange != null) {
                data.onChange.accept(online, datapoint);
            }
        }
    }
}
