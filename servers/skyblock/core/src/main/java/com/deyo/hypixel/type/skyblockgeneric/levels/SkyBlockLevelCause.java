package com.deyo.hypixel.type.skyblockgeneric.levels;

import lombok.NonNull;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategories;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategory;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.AccessoryComponent;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.*;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumRewards;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.fairysouls.FairySoulExchangeLevels;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyBlockLevelCause {
    private static final Map<String, SkyBlockLevelCauseAbstr> CAUSES = new HashMap<>();

    public static void initializeCauses() {
        // Register all Skill causes
        for (SkillCategories category : SkillCategories.values()) {
            for (int i = 1; i <= category.asCategory().getHighestLevel(); i++) {
                CAUSES.put("skill-" + category.name() + "-" + i, new SkillLevelCause(category, i));
            }
        }

        // Register all accessory causes
        for (ItemType itemTypeLinker : ItemType.values()) {
            if (new SkyBlockItem(itemTypeLinker).hasComponent(AccessoryComponent.class)) {
                CAUSES.put("accessory-" + itemTypeLinker.name(), new NewAccessoryLevelCause(itemTypeLinker));
            }
        }

        // Register all level causes up to 500
        for (int i = 0; i <= 500; i++) {
            CAUSES.put("level-" + i, new LevelCause(i));
        }

        // Register all collection causes
        for (ItemType itemTypeLinker : ItemType.values()) {
            CollectionCategory category = CollectionCategories.getCategory(itemTypeLinker);
            if (category == null) continue;

            for (CollectionCategory.ItemCollection collection : category.getCollections()) {
                for (CollectionCategory.ItemCollectionReward reward : collection.rewards()) {
                    CAUSES.put("collection-" + itemTypeLinker.name() + "-" + collection.getPlacementOf(reward),
                            new CollectionLevelCause(itemTypeLinker, collection.getPlacementOf(reward)));
                }
            }
        }

        // Register all Fairy Exchange rewards
        for (int i = 0; i <= FairySoulExchangeLevels.values().length; i++) {
            CAUSES.put("fairy-soul-exchange-" + i, new FairySoulExchangeLevelCause(i));
        }

        // Register all collected mob type rewards
        for (MobType mobType : MobType.values()) {
            CAUSES.put("mob-type-" + mobType.name(), new CollectedMobTypesCause(mobType.name()));
        }

        // Register all Mission rewards
        for (String missionID : MissionData.getAllMissionIDs()) {
            CAUSES.put("mission-" + missionID, new MissionLevelCause(missionID));
        }

        // Register all Museum rewards
        for (MuseumRewards reward : MuseumRewards.values()) {
            CAUSES.put("museum-" + reward.name(), new MuseumLevelCause(reward));
        }
    }

    public static Map<String, SkyBlockLevelCauseAbstr> getCauses() {
        return CAUSES;
    }

    public static Double getTotalXP() {
        double total = 0;
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            total += cause.xpReward();
        }
        return total;
    }

    public static FairySoulExchangeLevelCause getFairySoulExchangeCause(int exchangeCount) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("fairy-soul-exchange-" + exchangeCount);
        return cause instanceof FairySoulExchangeLevelCause exchangeCause ? exchangeCause : null;
    }

    public static MissionLevelCause getMissionCause(String missionKey) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("mission-" + missionKey);
        return cause instanceof MissionLevelCause missionCause ? missionCause : null;
    }

    public static MuseumLevelCause getMuseumCause(MuseumRewards reward) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("museum-" + reward.name());
        return cause instanceof MuseumLevelCause museumCause ? museumCause : null;
    }

    public static CollectionLevelCause getCollectionCause(ItemType itemTypeLinker, int level) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("collection-" + itemTypeLinker.name() + "-" + level);
        return cause instanceof CollectionLevelCause collectionCause ? collectionCause : null;
    }

    public static int getAmountOfCauses() {
        return CAUSES.size();
    }

    public static LevelCause getLevelCause(int level) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("level-" + level);
        return cause instanceof LevelCause levelCause ? levelCause : null;
    }

    public static NewAccessoryLevelCause getAccessoryCause(ItemType itemTypeLinker) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("accessory-" + itemTypeLinker.name());
        return cause instanceof NewAccessoryLevelCause accessoryCause ? accessoryCause : null;
    }

    public static SkillLevelCause getSkillCause(SkillCategories category, int level) {
        SkyBlockLevelCauseAbstr cause = CAUSES.get("skill-" + category.name() + "-" + level);
        return cause instanceof SkillLevelCause skillCause ? skillCause : null;
    }

    public static @Nullable SkyBlockLevelCauseAbstr getCause(String key) {
        return CAUSES.get(key);
    }

    public static String getKey(@NonNull SkyBlockLevelCauseAbstr cause) {
        for (Map.Entry<String, SkyBlockLevelCauseAbstr> entry : CAUSES.entrySet()) {
            if (entry.getValue() == cause) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Cause not found " + cause.getClass().getSimpleName());
    }

    public static List<SkyBlockLevelCauseAbstr> getSkillCauses(SkillCategories category, int levelUpToInclusive) {
        List<SkyBlockLevelCauseAbstr> causes = new ArrayList<>();
        for (SkyBlockLevelCauseAbstr cause : CAUSES.values()) {
            if (cause instanceof SkillLevelCause skillCause) {
                if (skillCause.getCategory() == category && skillCause.getLevel() <= levelUpToInclusive) {
                    causes.add(skillCause);
                }
            }
        }
        return causes;
    }
}
