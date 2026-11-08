package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.coop;

import com.deyo.hypixel.commons.data.DeyoData;
import com.deyo.hypixel.commons.skyblock.SkyBlockPlayerProfiles;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointBoolean;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointString;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.commons.skyblock.CoopLinks;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointUUID;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

final class CoopProfileCreation {

    private CoopProfileCreation() {}

    static UUID create(SkyBlockPlayer player, CoopDatabase.Coop coop) {
        UUID profileId = UUID.randomUUID();
        DeyoData.profile().link(profileId, CoopLinks.COOP, coop.coopUUID());

        SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(player.getUuid(), profileId);
        SkyBlockDataHandler source = coop.memberProfiles().isEmpty()
                ? null
                : sourceHandler(coop.memberProfiles().getFirst());

        if (source == null) {
            handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).setValue(UUID.randomUUID());
            handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class)
                    .setValue(SkyBlockPlayerProfiles.getRandomName());
        } else {
            handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class)
                    .setValue(source.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue());
            handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class)
                    .setValue(source.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
            handler.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class)
                    .setValue(source.get(SkyBlockDataHandler.Data.PROFILE_MODE, DatapointString.class).getValue());
        }

        handler.get(SkyBlockDataHandler.Data.IS_COOP, DatapointBoolean.class).setValue(true);

        new ProfilesDatabase(profileId.toString()).saveDocument(handler.toProfileDocument());
        return profileId;
    }

    private static SkyBlockDataHandler sourceHandler(UUID memberProfile) {
        ProfilesDatabase database = new ProfilesDatabase(memberProfile.toString());
        if (database.exists()) return SkyBlockDataHandler.createFromProfileOnly(database.getDocument());

        SkyBlockPlayer owner = SkyBlockGenericLoader.getPlayerFromProfileUUID(memberProfile);
        return owner == null ? null : SkyBlockDataHandler.getUser(owner.getUuid());
    }
}
