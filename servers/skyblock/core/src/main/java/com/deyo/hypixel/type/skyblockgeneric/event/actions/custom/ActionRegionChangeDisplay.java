package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockActionBar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionRegionChangeDisplay implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();

        if (event.getTo() != null && event.getFrom() != null
                && !event.getTo().equals(event.getFrom())) {

            RegionType type = event.getTo();
            String name = type.getName();
            if (type == RegionType.PLAYER_MUSEUM) {
                name = name.formatted(player.getUsername());
            }

            SkyBlockActionBar.getFor(player).addReplacement(
                    SkyBlockActionBar.BarSection.DEFENSE,
                    Text.of("<color:{}> ⏣ {}", type.getColor(), name),
                    20,
                    2
            );
        }
    }
}
