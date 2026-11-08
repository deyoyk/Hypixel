package com.deyo.hypixel.type.game.replay.dispatcher;

import com.deyo.hypixel.type.game.replay.ReplayRecorder;

public interface ReplayDispatcher {
	void initialize(ReplayRecorder recorder);

	default void tick() {
	}

	void cleanup();

	String getName();
}
