package com.deyo.hypixel.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.particle.Particle;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.Rarity;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributePetData;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointPetData;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.KatUpgrade;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.PetHandler;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.RarityValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class PetComponent extends SkyBlockItemComponent {
    private final String petName;
    private final RarityValue<Integer> georgePrice;
    private final RarityValue<KatUpgrade> katUpgrades;
    private final ItemStatistics baseStatistics;
    private final RarityValue<ItemStatistics> perLevelStatistics;
    private final Particle particleId;
    private final SkillCategories skillCategory;
    private final String skullTexture;
    private final String handlerId;

    public PetComponent(String petName, RarityValue<Integer> georgePrice,
                        @Nullable RarityValue<KatUpgrade> katUpgrades,
                        ItemStatistics baseStatistics, RarityValue<ItemStatistics> perLevelStatistics,
                        Particle particleId, String skillCategory, String skullTexture,
                        String handlerId) {
        this.petName = petName;
        this.georgePrice = georgePrice;
        this.katUpgrades = katUpgrades;
        this.baseStatistics = baseStatistics;
        this.perLevelStatistics = perLevelStatistics;
        this.particleId = particleId;
        this.skillCategory = SkillCategories.valueOf(skillCategory);
        this.skullTexture = skullTexture;
        this.handlerId = handlerId;

        addInheritedComponent(new SkullHeadComponent((item) -> skullTexture));
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new InteractableComponent(this::interact, this::interact, null));
        addInheritedComponent(new LoreUpdateComponent(
                new LoreConfig((item, player) -> getAbsoluteLore(player, item), (item, player) -> {
                    Rarity rarity = item.getAttributeHandler().getRarity();
                    int level = item.getAttributeHandler().getPetData().getAsLevel(rarity);
                    return Text.of("<7>[Lvl {}] <color:{}>{}", level, rarity.getColor(), petName).serialize();
                }), true)
        );
    }

    private void interact(SkyBlockPlayer player, SkyBlockItem item) {
        DatapointPetData.UserPetData petData = player.getPetData();
        ItemType type = item.getAttributeHandler().getPotentialType();
        Rarity rarity = item.getAttributeHandler().getRarity();

        if (petData.getPet(type) != null) {
            player.sendMessage("<c>You already have a pet of this type.");
            return;
        }

        petData.addPet(item);
        player.setItemInHand(null);
        player.sendMessage("<a>Successfully added {} <a>to your pet menu!", item.getDisplayName());
        player.playSound(Sound.sound()
                .type(Key.key("minecraft", "entity.experience_orb.pickup"))
                .volume(1f)
                .pitch(1f)
                .build());
    }

    public List<String> getAbsoluteLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>();
        ItemAttributePetData.PetData petData = item.getAttributeHandler().getPetData();
        Rarity rarity = item.getAttributeHandler().getRarity();
        int level = petData.getAsLevel(rarity);

        List<PetAbility> abilities = PetHandler.valueOf(handlerId.toUpperCase()).getAbilities(item);

        lore.add("<8>" + skillCategory.asCategory().getName() + " Pet");
        lore.add(" ");

        for (ItemStatistic stat : ItemStatistic.values()) {
            double value = baseStatistics.getOverall(stat)
                    + getPerLevelStatistics(rarity).getOverall(stat) * level;
            if (value == 0) continue;

            if (stat.getIsPercentage()) {
                addPropertyPercent(stat.getDisplayName(), value, lore);
            } else {
                addPropertyInt(stat.getDisplayName(), value, lore);
            }
        }

        for (PetAbility ability : abilities) {
            lore.add(" ");
            lore.add("<6>" + ability.getName());
            lore.addAll(ability.getDescription(item));
        }

        if (level < 100) {
            double experience = petData.getExperienceInCurrentLevel(rarity);
            int nextLevel = level + 1;
            long nextLevelExperience = petData.getExperienceForLevel(nextLevel, rarity);

            lore.add(" ");
            lore.add(progressText("Progress to Level " + nextLevel, experience, nextLevelExperience));
            lore.add(Text.of("<bar:{}:{}>", experience, nextLevelExperience).serialize());
        }

        lore.add(" ");
        lore.add(Text.of("<rarity:{}>", rarity.name()).serialize());

        return lore;
    }

    public ItemStatistics getPerLevelStatistics(Rarity rarity) {
        return perLevelStatistics.getForRarity(rarity);
    }

    private static String progressText(String label, double current, double max) {
        double percent = max != 0 ? (current / max) * 100.0 : 0.0;
        percent = StringUtility.roundTo(percent, 1);
        return percent < 100.0
                ? "<7>" + label + ": <e>" + StringUtility.commaify(percent) + "<6>%"
                : "<7>" + label + ": <a>100.0%";
    }

    private static void addPropertyInt(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("<7>" + name + ": <a>" + (value >= 0 ? "+" : "") + value);
        }
    }

    private static void addPropertyPercent(String name, double value, List<String> lore) {
        if (value != 0.0) {
            lore.add("<7>" + name + ": <a>" + (value >= 0 ? "+" : "") + value + "%");
        }
    }
}
