package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildMember;
import com.deyo.hypixel.commons.guild.GuildRank;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.v2.PaginatedView;
import com.deyo.hypixel.type.generic.gui.v2.ViewConfiguration;
import com.deyo.hypixel.type.generic.gui.v2.ViewLayout;
import com.deyo.hypixel.type.generic.gui.v2.context.ClickContext;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GUIGuildMembers extends PaginatedView<GuildMember, GUIGuildMembers.MembersState> {

    public enum MemberSort {
        LAST_ONLINE("Last Online"),
        GUILD_RANK("Guild Rank"),
        VETERANCY("Veterancy"),
        WEEKLY_GEXP("Weekly GEXP");

        private final String displayName;

        MemberSort(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public MemberSort next() {
            MemberSort[] values = values();
            return values[(ordinal() + 1) % values.length];
        }
    }

    private static final int[] MEMBER_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public ViewConfiguration<MembersState> configuration() {
        return new ViewConfiguration<>("Guild Members", InventoryType.CHEST_6_ROW);
    }

    @Override
    protected int[] getPaginatedSlots() {
        return MEMBER_SLOTS;
    }

    @Override
    protected int getNextPageSlot() {
        return 44;
    }

    @Override
    protected int getPreviousPageSlot() {
        return 36;
    }

    @Override
    protected void layoutCustom(ViewLayout<MembersState> layout, MembersState state, ViewContext ctx) {
        layout.slot(49, ItemStacks.item(Material.ARROW, """
                <a>Go Back
                <7>To Guild"""), (click, viewCtx) -> viewCtx.navigator().pop());
    }

    @Override
    protected ItemStack.Builder renderItem(GuildMember member, int index, HypixelPlayer player) {
        Text displayName = HypixelPlayer.getDisplayName(member.getUuid());

        return ItemStacks.item(Material.PLAYER_HEAD, 1, displayName, Text.of("""
                <7>Guild Rank: <b>{}
                <7>Member since: <b>{}
                <7>Weekly GEXP: <6>{:,}
                <7>Total GEXP: <6>{:,}""",
            member.getRankName(),
            formatDuration(System.currentTimeMillis() - member.getJoinedAt()),
            member.getWeeklyGexp(),
            member.getTotalGexp()).lines());
    }

    @Override
    protected void onItemClick(ClickContext<MembersState> click, ViewContext ctx, GuildMember item, int index) {
    }

    @Override
    protected boolean shouldFilterFromSearch(MembersState state, GuildMember item) {
        return false;
    }

    public static MembersState createState(GuildData guild) {
        return createState(guild, MemberSort.LAST_ONLINE, false);
    }

    public static MembersState createState(GuildData guild, MemberSort sort, boolean reversed) {
        return new MembersState(sortedMembers(guild, sort, reversed), 0);
    }

    static List<GuildMember> sortedMembers(GuildData guild, MemberSort sort, boolean reversed) {
        List<GuildMember> members = new ArrayList<>(guild.getMembers());
        Comparator<GuildMember> comparator = switch (sort) {
            case LAST_ONLINE -> Comparator.comparingLong(GuildMember::getLastLogin).reversed();
            case GUILD_RANK -> Comparator.comparingInt(member -> rankPriority(guild, member));
            case VETERANCY -> Comparator.comparingLong(GuildMember::getJoinedAt);
            case WEEKLY_GEXP -> Comparator.comparingLong(GuildMember::getWeeklyGexp).reversed();
        };
        if (reversed) {
            comparator = comparator.reversed();
        }
        members.sort(comparator.thenComparing(member -> member.getUuid().toString()));
        return members;
    }

    private static int rankPriority(GuildData guild, GuildMember member) {
        GuildRank rank = guild.getRank(member.getRankName());
        return rank == null ? Integer.MAX_VALUE : rank.getPriority();
    }

    private static String formatDuration(long millis) {
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        if (days > 0) return days + " days, " + hours + " hours ago";
        if (hours > 0) return hours + " hours ago";
        return "Just now";
    }

    public record MembersState(List<GuildMember> items, int page) implements PaginatedState<GuildMember> {
        @Override
        public PaginatedState<GuildMember> withPage(int page) {
            return new MembersState(items, page);
        }

        @Override
        public PaginatedState<GuildMember> withItems(List<GuildMember> items) {
            return new MembersState(items, page);
        }
    }
}
