package com.deyo.hypixel.type.skyblockgeneric.data;

import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.server.play.UpdateHealthPacket;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.data.DeyoData;
import com.deyo.hypixel.commons.skyblock.CoopLinks;
import com.deyo.hypixel.commons.skyblock.SkyBlockPlayerProfiles;
import com.deyo.hypixel.proxyapi.PlayerTransferDataCache;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointString;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointStringList;
import com.deyo.hypixel.type.generic.data.domain.DomainKey;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataDomain;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.generic.data.mongodb.UserDatabase;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.CustomGroups;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.generic.utility.MathUtility;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointUUID;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.PlayerRegionChangeEvent;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.island.SkyBlockIsland;
import com.deyo.hypixel.type.skyblockgeneric.warps.TravelScrollIslands;
import org.bson.Document;

import org.tinylog.Logger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class SkyBlockDomain implements PlayerDataDomain<SkyBlockDataHandler> {
    public static final DomainKey<SkyBlockDataHandler> KEY = new DomainKey<>("skyblock-profile", SkyBlockDataHandler.class);

    private static final Map<UUID, UUID> linkedCoops = new ConcurrentHashMap<>();

    @Override
    public DomainKey<SkyBlockDataHandler> key() {
        return KEY;
    }

    @Override
    public boolean appliesTo(ServerType type) {
        return type.isSkyBlock();
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void load(UUID uuid) {
        if (PlayerDataService.isLoaded(KEY, uuid)) return;

        UUID profileId = loadProfiles(uuid).getCurrentlySelected();
        SkyBlockDataHandler handler = SkyBlockDataHandler.initUserWithDefaultData(uuid, profileId);
        UUID coopId = resolveCoopId(profileId);
        DeyoData.profile().link(profileId, CoopLinks.COOP, coopId);
        linkedCoops.put(uuid, coopId);

        String transferred = PlayerTransferDataCache.takeProfileDocument(uuid);
        if (transferred != null) {
            handler.loadFromTransferDocument(Document.parse(transferred));
        } else {
            handler.loadFromApi();
        }

        DatapointUUID islandDatapoint = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class);
        if (islandDatapoint.getValue() == null) islandDatapoint.setValue(profileId);

        DatapointString profileNameDatapoint = handler.get(SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class);
        if (Objects.equals(profileNameDatapoint.getValue(), "null")) {
            profileNameDatapoint.setValue(SkyBlockPlayerProfiles.getRandomName());
        }

        PlayerDataService.store(KEY, uuid, handler);
    }

    @Override
    public void attach(HypixelPlayer player) {
        SkyBlockDataHandler handler = PlayerDataService.get(KEY, player.getUuid());
        UUID islandUuid = handler.get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
        ((SkyBlockPlayer) player).setSkyBlockIsland(SkyBlockIsland.getOrCreate(islandUuid, handler.getCurrentProfileId()));
    }

    @Override
    public void applyToPlayer(HypixelPlayer player) {
        SkyBlockPlayer sbp = (SkyBlockPlayer) player;
        PlayerDataService.get(KEY, player.getUuid()).runOnLoad(sbp);
        scheduleRegionRefresh(sbp);
        sendProfileIntro(sbp);
    }

    @Override
    public void save(HypixelPlayer player) {
        SkyBlockDataHandler handler = PlayerDataService.find(KEY, player.getUuid()).orElse(null);
        if (handler == null) return;
        handler.runOnSave((SkyBlockPlayer) player);
        handler.saveToApi();
    }

    @Override
    public void unload(UUID uuid) {
        SkyBlockDataHandler handler = PlayerDataService.find(KEY, uuid).orElse(null);
        if (handler != null) DeyoData.profile().unload(handler.getCurrentProfileId());
        PlayerDataService.evict(KEY, uuid);
        releaseCoopLink(uuid);
    }

    public static boolean isProfileHosted(UUID profileId) {
        for (SkyBlockDataHandler handler : PlayerDataService.loaded(KEY)) {
            if (profileId.equals(handler.getCurrentProfileId())) return true;
        }
        return false;
    }

    private static void releaseCoopLink(UUID uuid) {
        UUID coopId = linkedCoops.remove(uuid);
        if (coopId == null || linkedCoops.containsValue(coopId)) return;
        try {
            DeyoData.profile().unloadLink(CoopLinks.COOP, coopId);
        } catch (Exception e) {
            Logger.error(e, "Failed to release coop link container {}", coopId);
        }
    }

    public static String transferDocument(SkyBlockPlayer player) {
        SkyBlockDataHandler handler = PlayerDataService.find(KEY, player.getUuid()).orElse(null);
        if (handler == null) throw new IllegalStateException("SkyBlock profile data is not loaded");

        DeyoData.profile().flush(handler.getCurrentProfileId());

        UUID selectedProfileId = player.getProfiles().getCurrentlySelected();
        if (!handler.getCurrentProfileId().equals(selectedProfileId)) {
            Document selectedDocument = new ProfilesDatabase(selectedProfileId.toString()).getDocument();
            if (selectedDocument == null) {
                throw new IllegalStateException("Selected SkyBlock profile does not exist: " + selectedProfileId);
            }
            return selectedDocument.toJson();
        }

        return handler.toProfileDocument().toJson();
    }

    private static UUID resolveCoopId(UUID profileId) {
        CoopDatabase.Coop coop = CoopDatabase.getFromMemberProfile(profileId);
        return coop != null ? coop.coopUUID() : profileId;
    }

    private static SkyBlockPlayerProfiles loadProfiles(UUID uuid) {
        UserDatabase userDatabase = new UserDatabase(uuid);
        SkyBlockPlayerProfiles profiles = userDatabase.getProfiles();

        if (profiles.getCurrentlySelected() == null) {
            UUID profileId = UUID.randomUUID();
            profiles.setCurrentlySelected(profileId);
            profiles.addProfile(profileId);
            userDatabase.saveProfiles(profiles);
        }

        return profiles;
    }

    private static void scheduleRegionRefresh(SkyBlockPlayer player) {
        MathUtility.delay(() -> {
            SkyBlockRegion playerRegion = player.getRegion();
            if (playerRegion != null && player.isOnline()) {
                HypixelEventHandler.callCustomEvent(new PlayerRegionChangeEvent(player, null, playerRegion.getType()));
            }
        }, 50);
    }

    private static void sendProfileIntro(SkyBlockPlayer player) {
        Thread.startVirtualThread(() -> {
            player.sendMessage("");

            Rank rank = player.getRank();
            if (rank.isStaff()) {
                CustomGroups.staffMembers.add(player);
            }

            player.sendMessage("<7> ");
            player.sendMessage("<a>You are playing on profile: <e>{}", player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.PROFILE_NAME, DatapointString.class).getValue());
            player.sendMessage("<8>Profile ID: {}", player.getProfiles().getCurrentlySelected());

            UUID islandUuid = player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.ISLAND_UUID, DatapointUUID.class).getValue();
            if (!islandUuid.equals(player.getProfiles().getCurrentlySelected())) {
                player.sendMessage("<8>Island ID: {}", islandUuid);
            }
            player.sendMessage(" ");

            player.health = player.getMaxHealth();
            player.sendPacket(new UpdateHealthPacket((player.health / player.getMaxHealth()) * 20, 20, 20));

            MinecraftServer.getBossBarManager().removeAllBossBars(player);
            MathUtility.delay(() -> {
                if (!player.isOnline()) return;
                player.getPetData().updatePetEntityImpl(player);
            }, 20);

            TravelScrollIslands island = TravelScrollIslands.getFromType(HypixelConst.getTypeLoader().getType());
            if (island != null) {
                List<String> visitedIslands = player.getSkyblockDataHandler()
                        .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).getValue();
                if (!visitedIslands.contains(island.getInternalName())) {
                    visitedIslands.add(island.getInternalName());
                    player.getSkyblockDataHandler()
                            .get(SkyBlockDataHandler.Data.VISITED_ISLANDS, DatapointStringList.class).setValue(visitedIslands);
                }
            }
        });
    }
}
