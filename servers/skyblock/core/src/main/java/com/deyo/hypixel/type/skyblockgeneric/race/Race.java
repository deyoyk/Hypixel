package com.deyo.hypixel.type.skyblockgeneric.race;

import net.minestom.server.coordinate.Point;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public interface Race {
	String getId();
	String getTitle();
	Point startPosition();
	Point endPosition();
	List<Point> getCheckpoints();
	void onFinish(SkyBlockPlayer player, long time, boolean isBest);
	void onCheckpoint(SkyBlockPlayer player, int checkpointIndex, long time);
	int timeLimit();
	default boolean canStart(SkyBlockPlayer player) {
		return true;
	}
}
