package com.deyo.hypixel.type.replayviewer.playback.scoreboard;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.replayviewer.playback.ReplaySession;

import java.util.List;

public interface ReplayScoreboard {

    void create(Player viewer);

    void update(ReplaySession session);

    void remove(Player viewer);

    Text getTitle();

    List<Text> getLines(ReplaySession session);

}
