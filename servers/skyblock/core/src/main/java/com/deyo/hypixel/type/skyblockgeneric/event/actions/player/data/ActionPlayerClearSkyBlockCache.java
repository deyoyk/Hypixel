package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.ActionCharmingMobKill;
import com.deyo.hypixel.type.skyblockgeneric.event.actions.player.ActionPlayerStrayTooFar;
import com.deyo.hypixel.type.skyblockgeneric.event.actions.player.mobdamage.PlayerActionDamageMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumDisplays;
import com.deyo.hypixel.type.skyblockgeneric.server.eventcaller.CustomEventCaller;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.statistics.PlayerStatistics;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockScoreboard;

public class ActionPlayerClearSkyBlockCache implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.DISCONNECT, order = 10)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // remove from caches
        SkyBlockScoreboard.removeCache(player);
        player.getPetData().updatePetEntityImpl(null);
        PlayerItemOrigin.clearCache(player.getUuid());
        ActionPlayerStrayTooFar.startedStray.remove(player.getUuid());
        PlayerActionDamageMob.clearCooldown(player.getUuid());
        ActionCharmingMobKill.clearCooldown(player.getUuid());
        DroppedItemEntityImpl.clearDroppedItems(player);
        MuseumDisplays.clearDisplay(player.getUuid());
        PlayerStatistics.clearBossBar(player);
        CustomEventCaller.clearCache(player);    }
}
