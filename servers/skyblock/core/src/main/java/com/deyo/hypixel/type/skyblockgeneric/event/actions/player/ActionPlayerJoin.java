package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.calendar.SkyBlockCalendar;

public class ActionPlayerJoin implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.SPAWN)
    public void onPlayerJoin(PlayerSpawnEvent event) {
        int hour = SkyBlockCalendar.getHour();
        long minecraftTime = (hour * 1000L + 6000) % 24000;
        event.getPlayer().getInstance().setTime(minecraftTime);
    }

}
