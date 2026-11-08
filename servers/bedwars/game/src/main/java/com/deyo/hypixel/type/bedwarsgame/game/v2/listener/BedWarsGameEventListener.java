package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import com.deyo.hypixel.type.bedwarsgame.events.custom.BedWarsGameEventAdvanceEvent;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class BedWarsGameEventListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onGameEventAdvance(BedWarsGameEventAdvanceEvent event) {
        BedWarsGame game = (BedWarsGame) event.game();
        if (game == null) return;

        // update displays
        game.getGeneratorManager().updateDisplaysForEventChange();

        // Record to replay
        if (game.getReplayManager().isRecording()) {
            // Determine generator type and tier from event name
            String eventName = event.currentEvent().toLowerCase();
            if (eventName.contains("diamond")) {
                byte tier = 1;
                if (eventName.contains("ii")) tier = 2;
                else if (eventName.contains("iii")) tier = 3;
                game.getReplayManager().recordGeneratorUpgrade((byte) 0, tier);
            } else if (eventName.contains("emerald")) {
                byte tier = 1;
                if (eventName.contains("ii")) tier = 2;
                else if (eventName.contains("iii")) tier = 3;
                game.getReplayManager().recordGeneratorUpgrade((byte) 1, tier);
            }
        }
    }
}
