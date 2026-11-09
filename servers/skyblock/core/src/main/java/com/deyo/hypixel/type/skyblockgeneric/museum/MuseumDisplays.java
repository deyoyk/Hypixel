package com.deyo.hypixel.type.skyblockgeneric.museum;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.generic.entity.hologram.PlayerHolograms;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMuseum;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.museum.display.ItemMuseumDisplayHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public enum MuseumDisplays {
    ATRIUM_SLOTS(new ItemMuseumDisplayHandler(), List.of(MuseumableItemCategory.WEAPONS, MuseumableItemCategory.RARITIES),
        new Pos(33.5, 68.5, 23.5),
        new Pos(36.5, 68.5, 24.5),
        new Pos(38.5, 68.5, 26.5),
        new Pos(40.5, 68.5, 28.5),
        new Pos(41.5, 68.5, 31.5),
        new Pos(41.5, 68.5, 39.5),
        new Pos(40.5, 68.5, 42.5),
        new Pos(38.5, 68.5, 44.5),
        new Pos(36.5, 68.5, 46.5),
        new Pos(33.5, 68.5, 47.5),
        new Pos(25.5, 68.5, 47.5),
        new Pos(22.5, 68.5, 46.5),
        new Pos(20.5, 68.5, 44.5),
        new Pos(17.5, 68.5, 39.5),
        new Pos(17.5, 68.5, 31.5),
        new Pos(18.5, 68.5, 28.5),
        new Pos(20.5, 68.5, 26.5),
        new Pos(22.5, 68.5, 24.5),
        new Pos(25.5, 68.5, 23.5)
    );

    static final Map<UUID, Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram>> displayEntities = new HashMap<>();

    public static synchronized void clearDisplay(UUID playerId) {
        Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram> displays = displayEntities.remove(playerId);
        if (displays == null) return;
        displays.forEach((displayEntity, hologram) -> {
            if (hologram != null)
                PlayerHolograms.removeExternalPlayerHologram(hologram);
            if (displayEntity != null)
                displayEntity.remove();
        });
    }

    private final MuseumDisplay displayHandler;
    private final List<MuseumableItemCategory> allowedItemCategories;
    private final List<Pos> positions;

    MuseumDisplays(MuseumDisplay displayHandler, List<MuseumableItemCategory> allowedItemCategories, Pos... positions) {
        this.displayHandler = displayHandler;
        this.allowedItemCategories = allowedItemCategories;
        this.positions = List.of(positions);
    }

    @Override
    public String toString() {
        return StringUtility.toNormalCase(name().substring(0, name().indexOf("_")));
    }

    public static List<Pos> getAllPositions() {
        List<Pos> allPositions = new ArrayList<>();
        for (MuseumDisplays display : values()) {
            allPositions.addAll(display.positions);
        }
        return allPositions;
    }

    public static synchronized void updateDisplay(@NotNull SkyBlockPlayer player) {
        DatapointMuseum.MuseumData playerMuseumData = player.getMuseumData();
        UUID museumPlayerToView = playerMuseumData.getCurrentlyViewing().playerUuid();
        UUID museumProfileToView = playerMuseumData.getCurrentlyViewing().profileUuid();

        DatapointMuseum.MuseumData museumDataViewing;
        if (museumPlayerToView.equals(player.getUuid())) {
            museumDataViewing = playerMuseumData;
        } else {
            var profileDocument = ProfilesDatabase.fetchDocument(museumProfileToView);
            if (profileDocument == null) {
                museumDataViewing = playerMuseumData;
            } else {
                museumDataViewing = SkyBlockDataHandler.createFromProfileOnly(profileDocument)
                        .get(SkyBlockDataHandler.Data.MUSEUM_DATA, DatapointMuseum.class).getValue();
            }
        }

        if (displayEntities.containsKey(player.getUuid())) {
            clearDisplay(player.getUuid());
        }

        Map<MuseumDisplayEntityImpl, PlayerHolograms.ExternalPlayerHologram> newDisplayEntities = new HashMap<>();

        for (MuseumDisplays display : values()) {
            int totalPositions = display.positions.size();
            int[] handledPositions = new int[totalPositions];
            for (Map.Entry<Integer, List<SkyBlockItem>> entry : museumDataViewing.getDisplayHandler().fetchAllDisplayedItemsBySlot().get(display).entrySet()) {
                int position = entry.getKey();

                MuseumDisplayEntityInformation displayInfo = display.displayHandler.display(player, display, false, position);

                // Add all entities from the display info
                for (LivingEntity entity : displayInfo.getEntities()) {
                    newDisplayEntities.put((MuseumDisplayEntityImpl) entity, displayInfo.getHologram());
                }

                handledPositions[position] = 1;
            }

            for (int i = 0; i < totalPositions; i++) {
                if (handledPositions[i] == 0) {
                    MuseumDisplayEntityInformation displayInfo = display.displayHandler.display(player, display, true, i);
                    // Add all entities from the display info
                    for (LivingEntity entity : displayInfo.getEntities()) {
                        newDisplayEntities.put((MuseumDisplayEntityImpl) entity, displayInfo.getHologram());
                    }
                }
            }
        }

        displayEntities.put(player.getUuid(), newDisplayEntities);
    }
}
