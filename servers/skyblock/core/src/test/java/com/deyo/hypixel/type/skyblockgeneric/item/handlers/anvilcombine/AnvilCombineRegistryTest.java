package com.deyo.hypixel.type.skyblockgeneric.item.handlers.anvilcombine;

import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.FishingRodComponent;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnvilCombineRegistryTest {

    @BeforeAll
    static void registerItems() {
        ItemAttribute.registerItemAttributes();

        ConfigurableSkyBlockItem rod = new ConfigurableSkyBlockItem(
                ItemType.CHALLENGE_ROD.name(), Material.FISHING_ROD, List.of(), Map.of());
        rod.addComponent(new FishingRodComponent(), true);
        rod.register();
    }

    @Test
    void goldBottleCapUpgradesLuckOfTheSeaFromSixToSeven() {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler("GOLD_BOTTLE_CAP");
        assertNotNull(handler);

        SkyBlockItem rod = new SkyBlockItem(ItemType.CHALLENGE_ROD);
        rod.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 6));
        SkyBlockItem cap = new SkyBlockItem(ItemType.GOLD_BOTTLE_CAP);

        assertTrue(handler.validateFunction().canApply(null, rod, cap));

        handler.combineFunction().apply(rod, cap);

        SkyBlockEnchantment result = rod.getAttributeHandler().getEnchantment(EnchantmentType.LUCK_OF_THE_SEA);
        assertNotNull(result);
        assertEquals(7, result.level());
    }

    @Test
    void goldBottleCapRequiresLuckOfTheSeaSix() {
        AnvilCombineHandler handler = AnvilCombineRegistry.getHandler("GOLD_BOTTLE_CAP");
        SkyBlockItem rod = new SkyBlockItem(ItemType.CHALLENGE_ROD);
        rod.getAttributeHandler().addEnchantment(new SkyBlockEnchantment(EnchantmentType.LUCK_OF_THE_SEA, 5));
        SkyBlockItem cap = new SkyBlockItem(ItemType.GOLD_BOTTLE_CAP);

        assertFalse(handler.validateFunction().canApply(null, rod, cap));
    }
}
