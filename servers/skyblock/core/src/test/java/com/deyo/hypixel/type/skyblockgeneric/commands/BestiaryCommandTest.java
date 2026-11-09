package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.MinecraftServer;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.BestiaryCategories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BestiaryCommandTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void resolvesCategoryAndEntryFromAMobId() {
        BestiaryCategories category = BestiaryCommand.findCategory("INVISIBLE_CREEPER");

        assertEquals(BestiaryCategories.DEEP_CAVERNS, category);
        assertNotNull(BestiaryCommand.findEntry(category, "INVISIBLE_CREEPER"));
    }

    @Test
    void unknownMobIdsResolveToNull() {
        assertNull(BestiaryCommand.findCategory("NOT_A_MOB"));
    }
}
