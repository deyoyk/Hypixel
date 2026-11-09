package com.deyo.hypixel.type.game.replay;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.deyo.hypixel.commons.ServerType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ReplayMetadata {
	private UUID replayId;
	private String gameId;
	private ServerType serverType;
	private String serverId;
	private String gameTypeName;
	private String mapName;
	private String mapHash;
	private int version;
	private long startTime;
	private long endTime;
	private int durationTicks;
	private Map<UUID, String> players;
	private Map<String, List<UUID>> teams;
	private Map<String, TeamInfo> teamInfo;
	private String winnerId;
	private double mapCenterX;
	private double mapCenterZ;
	private long dataSize;

	public @Nullable String getTeamId(UUID playerUuid) {
		if (teams == null || playerUuid == null) {
			return null;
		}
		for (Map.Entry<String, List<UUID>> entry : teams.entrySet()) {
			List<UUID> members = entry.getValue();
			if (members != null && members.contains(playerUuid)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public record TeamInfo(
		String name,
		String colorCode,
		int color // RGB int
	) {
	}
}
