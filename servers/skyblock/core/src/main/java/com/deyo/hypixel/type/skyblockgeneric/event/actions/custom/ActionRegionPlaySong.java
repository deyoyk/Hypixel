package com.deyo.hypixel.type.skyblockgeneric.event.actions.custom;

import com.deyo.hypixel.commons.Songs;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.noteblock.SkyBlockSong;
import com.deyo.hypixel.type.skyblockgeneric.noteblock.SkyBlockSongsHandler;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockActionBar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class ActionRegionPlaySong implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerRegionChangeEvent event) {
        SkyBlockPlayer player = event.getPlayer();
        SkyBlockSongsHandler songHandler = player.getSongHandler();
        if (!SkyBlockSongsHandler.isEnabled) return;

        RegionType to = event.getTo();
        RegionType from = event.getFrom();
        if (to == null || (from != null && to.equals(from))) return;

        Songs songToPlay = null;
        List<Songs> regionSongs = to.getSongs();
        if (regionSongs != null && !regionSongs.isEmpty()) {
            songToPlay = regionSongs.get((int) (Math.random() * regionSongs.size()));
        } else if (HypixelConst.getTypeLoader() instanceof SkyBlockTypeLoader skyBlockTypeLoader) {
            songToPlay = skyBlockTypeLoader.getIslandSong();
        }

        SkyBlockSongsHandler.PlayerSong activeSong = songHandler.getPlayerSong();
        if (songToPlay == null) {
            if (activeSong != null) songHandler.stopPlayerSong();
            return;
        }

        if (activeSong != null && activeSong.song().getSong() == songToPlay) {
            return;
        }

        songHandler.setPlayerSong(new SkyBlockSong(songToPlay));

        SkyBlockActionBar.getFor(player).addReplacement(
            SkyBlockActionBar.BarSection.MANA,
            Text.of("<2>{}", StringUtility.toNormalCase(songToPlay.name())),
            20,
            2
        );
    }
}
