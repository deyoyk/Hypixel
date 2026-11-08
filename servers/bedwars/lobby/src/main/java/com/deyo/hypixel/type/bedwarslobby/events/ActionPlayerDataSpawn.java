package com.deyo.hypixel.type.bedwarslobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.bedwars.BedwarsLevelUtil;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarslobby.hologram.LeaderboardHologramManager;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLeaderboardLong;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.data.handlers.BedWarsDataHandler;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.quest.QuestData;
import com.deyo.hypixel.type.generic.quest.QuestDefinition;
import com.deyo.hypixel.type.generic.quest.QuestRegistry;
import com.deyo.hypixel.type.generic.quest.QuestType;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        if (!(HypixelConst.getTypeLoader().getType() == ServerType.BEDWARS_LOBBY)) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        Rank rank = player.getRank();
        if (rank == Rank.DEFAULT) return;

        for (HypixelPlayer onlinePlayer : HypixelGenericLoader.getLoadedPlayers()) {
            onlinePlayer.sendMessage("{} <6>joined the lobby!", player.getFullDisplayName());
        }

        BedWarsDataHandler handler = BedWarsDataHandler.getUser(player.getUuid());
        handler.runOnLoad(player);

		// display the player level progression in the experience bar
        DatapointLeaderboardLong dp = handler.get(BedWarsDataHandler.Data.EXPERIENCE, DatapointLeaderboardLong.class);
		player.setLevel(BedwarsLevelUtil.calculateLevel(dp.getValue()));
		player.setExp((float) BedwarsLevelUtil.calculateExperienceSinceLastLevel(dp.getValue()) / BedwarsLevelUtil.calculateMaxExperienceFromExperience(dp.getValue()));

		// Spawn leaderboard holograms for this player
		LeaderboardHologramManager.spawnHologramsForPlayer(player);

		// Auto-accept quests if enabled and player is MVP+
		if (player.getRank().isEqualOrHigherThan(Rank.MVP_PLUS) &&
			player.getToggles().get(DatapointToggles.Toggles.ToggleType.AUTO_ACCEPT_QUESTS)) {
			autoAcceptQuests(player, AchievementCategory.BEDWARS);
		}
    }

	private void autoAcceptQuests(HypixelPlayer player, AchievementCategory category) {
		QuestData questData = player.getQuestHandler().getQuestData();
		int startedCount = 0;

		// Auto-accept daily quests
		for (QuestDefinition quest : QuestRegistry.getByCategory(category, QuestType.DAILY)) {
			if (!questData.isActive(quest.getId()) && !questData.isCompleted(quest.getId())) {
				player.getQuestHandler().startQuest(quest.getId());
				startedCount++;
			}
		}

		// Auto-accept weekly quests
		for (QuestDefinition quest : QuestRegistry.getByCategory(category, QuestType.WEEKLY)) {
			if (!questData.isActive(quest.getId()) && !questData.isCompleted(quest.getId())) {
				player.getQuestHandler().startQuest(quest.getId());
				startedCount++;
			}
		}

		if (startedCount > 0) {
			player.sendMessage("<a>You have automatically started {} {} quest{}!",
					startedCount, category.getDisplayName(), startedCount > 1 ? "s" : "");
		}
	}
}