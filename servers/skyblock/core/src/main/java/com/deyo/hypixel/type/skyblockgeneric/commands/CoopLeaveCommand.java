package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.data.DeyoData;
import com.deyo.hypixel.commons.skyblock.SkyBlockPlayerProfiles;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.generic.data.mongodb.UserDatabase;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.commons.skyblock.CoopLinks;
import com.deyo.hypixel.type.skyblockgeneric.data.ProfileSwitcher;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

@CommandParameters(labels = "cooperativeleave",
        description = "Leaves the current coop",
        usage = "/coopleave",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopLeaveCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (!player.isCoop()) {
                player.sendMessage("<b>[Co-op] <c>You are not on a coop profile!");
                return;
            }

            CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());
            if (coop == null) {
                player.sendMessage("<b>[Co-op] <c>You are not part of a co-op!");
                return;
            }

            UUID coopId = coop.coopUUID();
            UUID playerId = player.getUuid();
            UUID profileId = player.getProfiles().getCurrentlySelected();
            UUID fallbackProfileId = player.getProfiles().getProfiles().stream()
                    .filter(candidate -> !candidate.equals(profileId))
                    .findFirst()
                    .orElse(null);

            if (fallbackProfileId == null) {
                player.sendMessage("<b>[Co-op] <c>You cannot leave your last profile!");
                player.sendMessage("<b>[Co-op] <e>Make another profile before deleting this one.");
                return;
            }

            ProfileSwitcher.switchTo(player, fallbackProfileId).thenAccept(result -> {
                if (result != ProfileSwitcher.Result.SWITCHED && result != ProfileSwitcher.Result.TRANSFERRED) return;
                leave(playerId, coopId, profileId);
            });
        });
    }

    private void leave(UUID playerId, UUID coopId, UUID profileId) {
        CoopDatabase.Coop remaining = CoopDatabase.update(coopId, latest -> {
            latest.members().remove(playerId);
            latest.removeInvite(playerId);
            latest.memberProfiles().remove(profileId);
        });

        DeyoData.profile().unlink(profileId, CoopLinks.COOP);
        if (remaining == null || (remaining.members().isEmpty() && remaining.memberProfiles().isEmpty())) {
            DeyoData.profile().deleteLink(CoopLinks.COOP, coopId);
        }

        ProfilesDatabase.deleteDocument(profileId.toString());

        SkyBlockPlayerProfiles profiles = new UserDatabase(playerId).getProfiles();
        profiles.removeProfile(profileId);
        new UserDatabase(playerId).saveProfiles(profiles);
    }
}
