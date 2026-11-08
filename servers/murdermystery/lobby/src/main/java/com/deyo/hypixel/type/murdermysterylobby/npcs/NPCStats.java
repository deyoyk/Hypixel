package com.deyo.hypixel.type.murdermysterylobby.npcs;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.murdermystery.MurderMysteryLeaderboardPeriod;
import com.deyo.hypixel.commons.murdermystery.MurderMysteryModeStats;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.achievement.AchievementRegistry;
import com.deyo.hypixel.type.generic.achievement.PlayerAchievementHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointMurderMysteryModeStats;
import com.deyo.hypixel.type.generic.data.handlers.MurderMysteryDataHandler;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.HumanConfiguration;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.murdermysterylobby.gui.GUIMurderMysteryStatistics;

public class NPCStats extends HypixelNPC {

    public NPCStats() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                // Get actual achievement count from player data
                PlayerAchievementHandler achievementHandler = new PlayerAchievementHandler(player);
                int playerAchievements = achievementHandler.getUnlockedCount(AchievementCategory.MURDER_MYSTERY);
                int totalAchievements = AchievementRegistry.getByCategory(AchievementCategory.MURDER_MYSTERY).size();

                // Get actual wins from player data
                long totalWins = 0;
                MurderMysteryDataHandler handler = MurderMysteryDataHandler.getUser(player);
                if (handler != null) {
                    MurderMysteryModeStats stats = handler.get(MurderMysteryDataHandler.Data.MODE_STATS, DatapointMurderMysteryModeStats.class).getValue();
                    totalWins = stats.getTotalWins(MurderMysteryLeaderboardPeriod.LIFETIME);
                }

                return new String[]{
                        "<6><l>Your Murder Mystery Profile",
                        Text.of("<f>Achievements: <e>{}<7>/<a>{}", playerAchievements, totalAchievements).serialize(),
                        Text.of("<f>Total Wins: <a>{:,}", totalWins).serialize(),
                        "<e><l>CLICK FOR STATS"
                };
            }

            @Override
            public String texture(HypixelPlayer player) {
                return player.getPlayerSkin().textures();
            }

            @Override
            public String signature(HypixelPlayer player) {
                return player.getPlayerSkin().signature();
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(3.5, 68, 10.5, -130, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return false;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        new GUIMurderMysteryStatistics().open(event.player());
    }
}
