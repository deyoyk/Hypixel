package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildMember;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIGuildMembersTest {

    @Test
    void sortModesUseGuildLocalData() {
        GuildData guild = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());
        guild.getMembers().clear();
        GuildMember member = member(guild, "Member", 200, 200, 10);
        GuildMember officer = member(guild, "Officer", 100, 300, 5);
        GuildMember master = member(guild, "Guild Master", 300, 100, 20);

        assertEquals(List.of(officer, member, master),
                GUIGuildMembers.sortedMembers(guild, GUIGuildMembers.MemberSort.LAST_ONLINE, false));
        assertEquals(List.of(officer, member, master),
                GUIGuildMembers.sortedMembers(guild, GUIGuildMembers.MemberSort.VETERANCY, false));
        assertEquals(List.of(master, officer, member),
                GUIGuildMembers.sortedMembers(guild, GUIGuildMembers.MemberSort.GUILD_RANK, false));
        assertEquals(List.of(master, member, officer),
                GUIGuildMembers.sortedMembers(guild, GUIGuildMembers.MemberSort.WEEKLY_GEXP, false));
        assertEquals(List.of(master, member, officer),
                GUIGuildMembers.sortedMembers(guild, GUIGuildMembers.MemberSort.LAST_ONLINE, true));
    }

    @Test
    void sortModeCyclesThroughEveryOption() {
        GUIGuildMembers.MemberSort sort = GUIGuildMembers.MemberSort.LAST_ONLINE;

        for (int i = 0; i < GUIGuildMembers.MemberSort.values().length; i++) {
            sort = sort.next();
        }

        assertEquals(GUIGuildMembers.MemberSort.LAST_ONLINE, sort);
    }

    private static GuildMember member(GuildData guild, String rank, long joinedAt, long lastLogin, long weeklyGexp) {
        GuildMember member = new GuildMember(UUID.randomUUID(), rank, joinedAt);
        member.setLastLogin(lastLogin);
        member.setWeeklyGexp(weeklyGexp);
        guild.getMembers().add(member);
        return member;
    }
}
