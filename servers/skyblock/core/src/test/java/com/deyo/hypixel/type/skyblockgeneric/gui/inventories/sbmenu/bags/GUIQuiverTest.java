package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bags;

import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.collection.CustomCollectionAward;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIQuiverTest {

    @BeforeAll
    static void registerAttributes() {
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void emptySlotsIncludeAirItems() {
        assertTrue(GUIQuiver.isEmptySlot(new SkyBlockItem(Material.AIR)));
        assertFalse(GUIQuiver.isEmptySlot(new SkyBlockItem(Material.COBBLESTONE)));
    }

    @Test
    void noUpgradesLockEverySlotInUpgradeOrder() {
        List<GUIQuiver.QuiverSlot> slots = GUIQuiver.calculateSlots(award -> false);

        assertEquals(36, slots.size());
        for (int i = 0; i < slots.size(); i++) {
            assertEquals(i, slots.get(i).index());
            assertFalse(slots.get(i).unlocked());
        }
        assertEquals(CustomCollectionAward.QUIVER, slots.get(0).upgrade());
        assertEquals(CustomCollectionAward.QUIVER_UPGRADE_1, slots.get(18).upgrade());
        assertEquals(CustomCollectionAward.QUIVER_UPGRADE_2, slots.get(27).upgrade());
    }

    @Test
    void eachOwnedUpgradeUnlocksOnlyItsPrefixOfSlots() {
        Set<CustomCollectionAward> owned = EnumSet.of(CustomCollectionAward.QUIVER);
        List<GUIQuiver.QuiverSlot> slots = GUIQuiver.calculateSlots(owned::contains);

        assertUnlocked(slots, 0, 18);
        assertLocked(slots, 18, 27, CustomCollectionAward.QUIVER_UPGRADE_1);
        assertLocked(slots, 27, 36, CustomCollectionAward.QUIVER_UPGRADE_2);

        owned.add(CustomCollectionAward.QUIVER_UPGRADE_1);
        slots = GUIQuiver.calculateSlots(owned::contains);
        assertUnlocked(slots, 0, 27);
        assertLocked(slots, 27, 36, CustomCollectionAward.QUIVER_UPGRADE_2);

        owned.add(CustomCollectionAward.QUIVER_UPGRADE_2);
        slots = GUIQuiver.calculateSlots(owned::contains);
        assertUnlocked(slots, 0, 36);
    }

    @Test
    void laterUpgradeCannotSkipAMissingEarlierUpgrade() {
        Set<CustomCollectionAward> owned = EnumSet.of(
                CustomCollectionAward.QUIVER, CustomCollectionAward.QUIVER_UPGRADE_2);
        List<GUIQuiver.QuiverSlot> slots = GUIQuiver.calculateSlots(owned::contains);

        assertUnlocked(slots, 0, 18);
        assertLocked(slots, 18, 27, CustomCollectionAward.QUIVER_UPGRADE_1);
        assertLocked(slots, 27, 36, CustomCollectionAward.QUIVER_UPGRADE_2);
    }

    private static void assertUnlocked(List<GUIQuiver.QuiverSlot> slots, int from, int to) {
        for (int i = from; i < to; i++) {
            assertTrue(slots.get(i).unlocked(), "slot " + i + " should be unlocked");
        }
    }

    private static void assertLocked(List<GUIQuiver.QuiverSlot> slots, int from, int to,
                                     CustomCollectionAward expectedUpgrade) {
        for (int i = from; i < to; i++) {
            assertFalse(slots.get(i).unlocked(), "slot " + i + " should be locked");
            assertEquals(expectedUpgrade, slots.get(i).upgrade());
        }
    }
}
