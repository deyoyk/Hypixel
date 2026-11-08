package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CalenderHourlyUpdateEvent;

public class ActionChangeTimeCalender implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(CalenderHourlyUpdateEvent event) {
        int hour = event.hour();
        long minecraftTime = (hour * 1000L + 6000) % 24000;

        SkyBlockGenericLoader.getLoadedPlayers().forEach(player -> {
            player.getInstance().setTime(minecraftTime);
        });
    }
}
