package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatapointCloneIndependenceTest {

    @BeforeAll
    static void registerAttributes() {
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void accessoryBagCloneDoesNotShareMutableCollections() {
        DatapointAccessoryBag original = new DatapointAccessoryBag("test");
        original.getValue().getAccessoryMap().put(1, null);
        original.getValue().getDiscoveredAccessories().add(ItemType.COBBLESTONE);

        DatapointAccessoryBag clone = (DatapointAccessoryBag) original.deepClone();
        clone.getValue().getAccessoryMap().clear();
        clone.getValue().getDiscoveredAccessories().clear();

        assertEquals(1, original.getValue().getAccessoryMap().size());
        assertEquals(List.of(ItemType.COBBLESTONE), original.getValue().getDiscoveredAccessories());
    }

    @Test
    void quiverCloneDoesNotShareItsMap() {
        DatapointQuiver original = new DatapointQuiver("test");
        original.getValue().getQuiverMap().put(1, null);

        DatapointQuiver clone = (DatapointQuiver) original.deepClone();
        clone.getValue().getQuiverMap().clear();

        assertTrue(original.getValue().getQuiverMap().containsKey(1));
    }

    @Test
    void quiverCloneCopiesStoredItems() {
        DatapointQuiver original = new DatapointQuiver("test");
        original.getValue().getQuiverMap().put(1, new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointQuiver clone = (DatapointQuiver) original.deepClone();
        clone.getValue().getQuiverMap().get(1).setAmount(1);

        assertEquals(5, original.getValue().getQuiverMap().get(1).getAmount());
    }

    @Test
    void accessoryBagCloneCopiesStoredItems() {
        DatapointAccessoryBag original = new DatapointAccessoryBag("test");
        original.getValue().getAccessoryMap().put(1, new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointAccessoryBag clone = (DatapointAccessoryBag) original.deepClone();
        clone.getValue().getAccessoryMap().get(1).setAmount(1);

        assertEquals(5, original.getValue().getAccessoryMap().get(1).getAmount());
    }

    @Test
    void sackOfSacksCloneCopiesStoredItems() {
        DatapointSackOfSacks original = new DatapointSackOfSacks("test");
        original.getValue().setInSlot(1, new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointSackOfSacks clone = (DatapointSackOfSacks) original.deepClone();
        clone.getValue().getInSlot(1).setAmount(1);

        assertEquals(5, original.getValue().getInSlot(1).getAmount());
    }

    @Test
    void petDataCloneDoesNotShareThePetsMap() {
        DatapointPetData original = new DatapointPetData("test");
        original.getValue().getPetsMap().put(null, true);

        DatapointPetData clone = (DatapointPetData) original.deepClone();
        clone.getValue().getPetsMap().clear();

        assertEquals(1, original.getValue().getPetsMap().size());
    }

    @Test
    void skillsCloneDoesNotShareTheSkillMap() {
        DatapointSkills original = new DatapointSkills("test");
        original.getValue().getSkills().put(SkillCategories.FARMING, 10.0);

        DatapointSkills clone = (DatapointSkills) original.deepClone();
        clone.getValue().getSkills().clear();

        assertEquals(10.0, original.getValue().getSkills().get(SkillCategories.FARMING));
    }

    @Test
    void minionDataCloneDeepCopiesCraftedTiers() {
        DatapointMinionData original = new DatapointMinionData("test");
        original.getValue().craftedMinions().add(Map.entry("COBBLESTONE", new ArrayList<>(List.of(1))));

        DatapointMinionData clone = (DatapointMinionData) original.deepClone();
        clone.getValue().craftedMinions().getFirst().getValue().add(2);

        assertEquals(List.of(1), original.getValue().craftedMinions().getFirst().getValue());
    }

    @Test
    void backpacksCloneDoesNotShareItsMap() {
        DatapointBackpacks original = new DatapointBackpacks("test");
        original.getValue().getBackpacks().put(1, null);

        DatapointBackpacks clone = (DatapointBackpacks) original.deepClone();
        clone.getValue().getBackpacks().clear();

        assertEquals(1, original.getValue().getBackpacks().size());
    }

    @Test
    void auctionStatisticsCloneDoesNotShareItsMap() {
        DatapointAuctionStatistics original = new DatapointAuctionStatistics("test");
        original.getValue().getStatistics().put(
                DatapointAuctionStatistics.AuctionStatistics.AuctionStat.values()[0], 1);

        DatapointAuctionStatistics clone = (DatapointAuctionStatistics) original.deepClone();
        clone.getValue().getStatistics().clear();

        assertEquals(1, original.getValue().getStatistics().size());
    }

    @Test
    void inventoryCloneDoesNotShareItsItemMap() {
        DatapointInventory original = new DatapointInventory("test");
        original.getValue().getItems().put(1, null);

        DatapointInventory clone = (DatapointInventory) original.deepClone();
        clone.getValue().getItems().clear();

        assertEquals(1, original.getValue().getItems().size());
    }

    @Test
    void inventoryCloneCopiesStoredItems() {
        DatapointInventory original = new DatapointInventory("test");
        SkyBlockItem item = new SkyBlockItem(ItemType.COBBLESTONE);
        original.getValue().getItems().put(1, item.toUnderstandable());
        original.getValue().setHelmet(item.toUnderstandable());

        DatapointInventory clone = (DatapointInventory) original.deepClone();

        assertNotSame(original.getValue().getItems().get(1), clone.getValue().getItems().get(1));
        assertNotSame(original.getValue().getHelmet(), clone.getValue().getHelmet());
    }

    @Test
    void katCloneCopiesStoredItems() {
        DatapointKat original = new DatapointKat("test");
        original.getValue().katMap().put(1L, new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointKat clone = (DatapointKat) original.deepClone();
        clone.getValue().katMap().get(1L).setAmount(1);

        assertEquals(5, original.getValue().katMap().get(1L).getAmount());
    }

    @Test
    void stashCloneCopiesStoredItems() {
        DatapointStash original = new DatapointStash("test");
        original.getValue().getItemStash().add(new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointStash clone = (DatapointStash) original.deepClone();
        clone.getValue().getItemStash().getFirst().setAmount(1);

        assertEquals(5, original.getValue().getItemStash().getFirst().getAmount());
    }

    @Test
    void auctionEscrowCloneCopiesStoredItem() {
        DatapointAuctionEscrow original = new DatapointAuctionEscrow("test");
        original.getValue().setItem(new SkyBlockItem(ItemType.COBBLESTONE, 5));

        DatapointAuctionEscrow clone = (DatapointAuctionEscrow) original.deepClone();
        clone.getValue().getItem().setAmount(1);

        assertEquals(5, original.getValue().getItem().getAmount());
    }

    @Test
    void bankDataCloneCopiesVaultItems() {
        DatapointBankData original = new DatapointBankData("test");
        original.getValue().getPersonalVault()[0] = new SkyBlockItem(ItemType.COBBLESTONE, 5);

        DatapointBankData clone = (DatapointBankData) original.deepClone();
        clone.getValue().getPersonalVault()[0].setAmount(1);

        assertEquals(5, original.getValue().getPersonalVault()[0].getAmount());
    }

    @Test
    void backpacksCloneCopiesStoredItems() {
        DatapointBackpacks original = new DatapointBackpacks("test");
        original.getValue().getBackpacks().put(
                1, new SkyBlockItem(ItemType.COBBLESTONE).toUnderstandable());

        DatapointBackpacks clone = (DatapointBackpacks) original.deepClone();

        assertNotSame(original.getValue().getBackpacks().get(1),
                clone.getValue().getBackpacks().get(1));
    }

    @Test
    void storageCloneCopiesStoredItems() {
        DatapointStorage original = new DatapointStorage("test");
        original.getValue().addPage(1);
        original.getValue().getPage(1).items[0] = new SkyBlockItem(ItemType.COBBLESTONE, 5);

        DatapointStorage clone = (DatapointStorage) original.deepClone();
        clone.getValue().getPage(1).items[0].setAmount(1);

        assertEquals(5, original.getValue().getPage(1).items[0].getAmount());
    }

    @Test
    void skyBlockExperienceCloneDoesNotShareItsCauseList() {
        DatapointSkyBlockExperience original = new DatapointSkyBlockExperience("test",
                new DatapointSkyBlockExperience.PlayerSkyBlockExperience(new ArrayList<>(), null));
        original.getValue().getCompletedExperienceCauses().add(null);

        DatapointSkyBlockExperience clone = (DatapointSkyBlockExperience) original.deepClone();
        clone.getValue().getCompletedExperienceCauses().clear();

        assertEquals(1, original.getValue().getCompletedExperienceCauses().size());
    }
}
