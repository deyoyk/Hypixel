package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom.levels;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.actions.player.ActionAddSkyBlockXPToNametag;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.SkyBlockXPModificationEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelRequirement;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelUnlock;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.LevelCause;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class ActionChangeHypixelXP implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(SkyBlockXPModificationEvent event) {
        if (event.getNewXP() <= event.getOldXP()) return;
        SkyBlockPlayer player = event.getPlayer();

        SkyBlockLevelRequirement oldLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getOldXP());
        SkyBlockLevelRequirement newLevel = SkyBlockLevelRequirement.getFromTotalXP(event.getNewXP());

        if (oldLevel == newLevel) {
            if (event.getCause().shouldDisplayMessage(player))
                player.sendMessage("<b>+<3>{} <b>SkyBlock XP", event.getNewXP() - event.getOldXP());
        } else {
            ActionAddSkyBlockXPToNametag.updatePlayerNametag(player);
            if (!(event.getCause() instanceof LevelCause)) {
                player.getSkyBlockExperience().addExperience(
                        SkyBlockLevelCause.getLevelCause(newLevel.asInt())
                );
            }
            List<SkyBlockLevelUnlock> unlocks = newLevel.getUnlocks();

            player.sendMessage("<3><m>---------------------------------");
            player.sendMessage("  <b><l>SKYBLOCK LEVEL UP! </l><7>{} <8><l>-> </l><3>{}", oldLevel.asInt(), newLevel.asInt());
            if (!unlocks.isEmpty()) {
                player.sendMessage(" ");
                player.sendMessage("  <a><l>REWARDS");
                unlocks.forEach(unlock -> {
                    unlock.getDisplay(player, newLevel.asInt()).forEach(line -> {
                        player.sendMessage(Text.of("  <8>").append(line));
                    });
                });
            }
            player.sendMessage("<3><m>---------------------------------");
        }
    }
}
