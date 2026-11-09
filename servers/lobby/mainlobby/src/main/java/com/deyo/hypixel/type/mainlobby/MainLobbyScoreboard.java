package com.deyo.hypixel.type.mainlobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.scoreboard.HypixelScoreboard;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.utility.NetworkCountCache;
import com.deyo.hypixel.type.generic.utility.SocialCountCache;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainLobbyScoreboard {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final Text BLANK = Text.literal(" ");
    private static final HypixelScoreboard scoreboard = new HypixelScoreboard();
    private static int prototypeName = 0;

    public static void start() {
        Scheduler scheduler = MinecraftServer.getSchedulerManager();

        scheduler.submitTask(() -> {
            prototypeName++;
            if (prototypeName > 50) {
                prototypeName = 0;
            }

            List<HypixelPlayer> players = List.copyOf(HypixelGenericLoader.getLoadedPlayers());
            String date = DATE_FORMAT.format(LocalDateTime.now(ZoneId.systemDefault()));
            Text sidebarName = getSidebarName(prototypeName);
            int networkPlayers = NetworkCountCache.getPlayerCount();
            int lobbyNumber = lobbyNumber();

            for (HypixelPlayer player : players) {
                HypixelDataHandler dataHandler = player.getDataHandler();

                if (dataHandler == null) {
                    continue;
                }

                List<Text> lines = new ArrayList<>();
                lines.add(Text.key("scoreboard.common.date_line", date, HypixelConst.getServerName()));
                lines.add(BLANK);
                lines.add(Text.key("scoreboard.main_lobby.rank", player.getRankTitle()));
                lines.add(Text.key("scoreboard.main_lobby.achievements",
                        StringUtility.commaify(player.getAchievementHandler().getTotalUnlockedCount())));
                lines.add(Text.key("scoreboard.main_lobby.level", StringUtility.commaify(player.getLevel())));
                lines.add(BLANK);
                lines.add(Text.key("scoreboard.main_lobby.lobby", StringUtility.commaify(lobbyNumber)));
                lines.add(Text.key("scoreboard.main_lobby.online_players", StringUtility.commaify(networkPlayers)));
                lines.add(BLANK);
                lines.add(Text.key("scoreboard.main_lobby.friends_online",
                        StringUtility.commaify(SocialCountCache.getFriendsOnline(player))));
                lines.add(Text.key("scoreboard.main_lobby.guild_online",
                        StringUtility.commaify(SocialCountCache.getGuildOnline(player))));
                lines.add(Text.key("scoreboard.common.footer"));

                scoreboard.update(player, sidebarName, lines);
            }
            return TaskSchedule.tick(4);
        });
    }

    public static void removeCache(Player player) {
        scoreboard.removeScoreboard(player);
        SocialCountCache.clear(player.getUuid());
    }

    static int lobbyNumber() {
        String serverName = HypixelConst.getServerName();
        if (serverName == null) return 0;
        int number = 0;
        boolean found = false;
        for (int index = 0; index < serverName.length(); index++) {
            char character = serverName.charAt(index);
            if (character >= '0' && character <= '9') {
                number = number * 10 + (character - '0');
                found = true;
            }
        }
        return found ? number : 0;
    }

    private static Text getSidebarName(int counter) {
        String baseText = "Hypixel";
        String[] colors = {"<f><l>", "<6><l>", "<e><l>"};
        String endColor = "<a><l>";

        String markup;
        if (counter > 0 && counter <= 8) {
            markup = colors[0] + baseText.substring(0, counter - 1) +
                colors[1] + baseText.charAt(counter - 1) +
                colors[2] + baseText.substring(counter) +
                endColor;
        } else if ((counter >= 9 && counter <= 19) ||
            (counter >= 25 && counter <= 29)) {
            markup = colors[0] + baseText + endColor;
        } else {
            markup = colors[2] + baseText + endColor;
        }
        return Text.of(markup);
    }
}
