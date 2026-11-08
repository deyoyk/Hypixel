package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.bedwars.LeaderboardPreferences;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointLeaderboardPreferences extends Datapoint<LeaderboardPreferences> {
	private static final JacksonSerializer<LeaderboardPreferences> serializer =
			new JacksonSerializer<>(LeaderboardPreferences.class);

	public DatapointLeaderboardPreferences(String key, LeaderboardPreferences value) {
		super(key, value, serializer);
	}

	public DatapointLeaderboardPreferences(String key) {
		this(key, LeaderboardPreferences.defaults());
	}
}
