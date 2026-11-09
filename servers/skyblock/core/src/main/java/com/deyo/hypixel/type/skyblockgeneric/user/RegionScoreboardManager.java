package com.deyo.hypixel.type.skyblockgeneric.user;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.commons.text.TextBody;
import com.deyo.hypixel.type.skyblockgeneric.elections.ElectionData;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class RegionScoreboardManager {
    private static final Map<RegionType, RegionScoreboard> BOARDS = Map.of(
            RegionType.ELECTION_ROOM,
            new RegionScoreboard(RegionScoreboardManager::renderElectionRoom, true)
    );

    private RegionScoreboardManager() {
    }

    public static @Nullable RegionScoreboard get(@Nullable RegionType type) {
        return type == null ? null : BOARDS.get(type);
    }

    private static void renderElectionRoom(Context context) {
        TextBody.Section election = context.body().section("election");
        election.line(Text.literal(" "));
        election.line(Text.key("scoreboard.skyblock.election_votes_title", context.calendarYear()));
        context.candidates().forEach(candidate -> {
            long votes = context.totalVotes().getOrDefault(candidate.getMayorName(), 0L);
            int barLength = context.maxVotes() > 0
                    ? (int) Math.round((votes * 15.0) / context.maxVotes())
                    : 0;
            Text bars = Text.of("<color:{0}>{1}", candidate.getColor(), "|".repeat(barLength))
                    .append("<f>{}", "|".repeat(15 - barLength));
            election.line(Text.key("scoreboard.skyblock.election_candidate_line",
                    bars, candidate.getColoredName()));
        });
    }

    public record RegionScoreboard(Consumer<Context> renderer, boolean hidesDefaultSections) {
    }

    public record Context(
            TextBody body,
            int calendarYear,
            List<ElectionData.CandidateData> candidates,
            Map<String, Long> totalVotes,
            long maxVotes
    ) {
    }
}
