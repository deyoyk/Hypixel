package com.deyo.hypixel.type.skyblockgeneric.data.monogdb;

import com.deyo.hypixel.proxyapi.ProxyPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public final class CoopInviteService {
    private CoopInviteService() {
    }

    public static boolean invite(SkyBlockPlayer player, SkyBlockPlayer target) {
        return invite(player, List.of(new ProxyPlayer(target.getUuid())), List.of(target.getUsername()));
    }

    public static boolean invite(SkyBlockPlayer player, List<ProxyPlayer> invitedPlayers, List<String> names) {
        if (invitedPlayers.isEmpty() || invitedPlayers.size() != names.size()) return false;

        if (invitedPlayers.size() > 4) {
            player.sendMessage("<b>[Co-op] <c>You can't invite more than 4 players!");
            return false;
        }

        if (invitedPlayers.stream().anyMatch(invited -> invited.uuid().equals(player.getUuid()))) {
            player.sendMessage("<c>You can't invite yourself to a co-op!");
            return false;
        }

        if (invitedPlayers.stream().map(ProxyPlayer::uuid).distinct().count() != invitedPlayers.size()) {
            player.sendMessage("<b>[Co-op] <c>You can't invite the same player twice!");
            return false;
        }

        if (hasExistingInvite(player)) return false;

        if (invitedPlayers.stream().anyMatch(invited -> CoopDatabase.getFromMember(invited.uuid()) != null)) {
            player.sendMessage("<b>[Co-op] <c>One or more of the players you specified already have a co-op or an invite pending!");
            return false;
        }

        CoopDatabase.Coop coop = CoopDatabase.getClean(player.getUuid());
        for (int i = 0; i < invitedPlayers.size(); i++) {
            ProxyPlayer invited = invitedPlayers.get(i);
            coop.addInvite(invited.uuid());
            player.sendMessage("<b>[Co-op] <e>You invited {} to a SkyBlock co-op!", names.get(i));
            invited.sendMessage("<b>----------------------------------------");
            invited.sendMessage("{} <e>invited you to a SkyBlock co-op!", player.getFullDisplayName());
            invited.sendMessage("<hover:'<e>Click here to view the invite'><click:run:'/coopcheck'>" +
                    "<6>Click here <e>to view!</click></hover>");
            invited.sendMessage("<b>----------------------------------------");
        }

        player.sendMessage("<hover:'<e>Click here to view the invite'><click:run:'/coopcheck'>" +
                "<e>Use <b>/coop <e>or <a><l>CLICK THIS </l><e>for status!</click></hover>");
        coop.save();
        return true;
    }

    public static boolean hasExistingInvite(SkyBlockPlayer player) {
        CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());

        if (coop != null) {
            if (coop.members().contains(player.getUuid())) {
                player.sendMessage("<c>You are already in a co-op!");
                player.sendMessage("<e>Run <a>/coopleave <e>to leave your current co-op.");
                return true;
            }

            if (coop.isOriginator(player.getUuid())) {
                player.sendMessage("<hover:'<e>Click here to view the invite'><click:run:'/coopcheck'>" +
                        "<e>You already have an outgoing co-op invite! <a><l>CLICK TO VIEW!</click></hover>");
            } else {
                player.sendMessage("<hover:'<e>Click here to view the invite'><click:run:'/coopcheck'>" +
                        "<c>You already have an incoming co-op invite! <a><l>CLICK TO VIEW!</click></hover>");
            }
            return true;
        }
        return false;
    }
}
