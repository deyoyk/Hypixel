package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.Rarity;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.deyo.hypixel.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.PetComponent;
import com.deyo.hypixel.type.skyblockgeneric.utility.RarityValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatapointPetDataTest {

    @BeforeAll
    static void registerItems() {
        ItemAttribute.registerItemAttributes();
        ConfigurableSkyBlockItem config = new ConfigurableSkyBlockItem(
                ItemType.BEE_PET.name(), Material.PLAYER_HEAD, List.of(), Map.of());
        config.addComponent(new PetComponent(
                "Bee",
                RarityValue.zeroInteger(),
                null,
                ItemStatistics.empty(),
                new RarityValue<>(ItemStatistics.empty(), ItemStatistics.empty(), ItemStatistics.empty(),
                        ItemStatistics.empty(), ItemStatistics.empty(), ItemStatistics.empty(),
                        ItemStatistics.empty()),
                Particle.FLAME,
                "FARMING",
                "texture",
                "BEE"), true);
        config.register();
    }

    @Test
    void refreshingAbilitiesPicksUpRarityChanges() {
        SkyBlockItem bee = new SkyBlockItem(ItemType.BEE_PET);
        HashMap<SkyBlockItem, Boolean> pets = new HashMap<>();
        pets.put(bee, true);
        DatapointPetData.UserPetData data = new DatapointPetData.UserPetData(pets);

        int mythicAbilities = data.getCachedAbilities(bee).size();
        bee.getAttributeHandler().setRarity(Rarity.COMMON);
        data.refreshCachedAbilities();

        assertEquals(1, data.getCachedAbilities(bee).size());
        assertEquals(4, mythicAbilities);
    }
}
