package com.deyo.hypixel.type.hub.events;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum.GUIMuseumEmptyDisplay;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.museum.GUIMuseumNonEmptyDisplay;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumDisplayEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickMuseumDisplay implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MuseumDisplayEntityImpl museumDisplayEntity) {
            boolean isEmpty = museumDisplayEntity.isEmpty();

            if (isEmpty) {
                new GUIMuseumEmptyDisplay(
                        museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()
                ).open(player);
            } else {
                new GUIMuseumNonEmptyDisplay(
                        museumDisplayEntity.getDisplay(),
                        museumDisplayEntity.getPositionInMuseum()
                ).open(player);
            }
        }
    }
}
