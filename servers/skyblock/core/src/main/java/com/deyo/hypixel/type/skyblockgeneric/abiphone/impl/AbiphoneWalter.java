package com.deyo.hypixel.type.skyblockgeneric.abiphone.impl;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointToggles;
import com.deyo.hypixel.type.generic.entity.npc.NPCOption;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.abiphone.AbiphoneNPC;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategories;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategory;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Collections;
import java.util.List;

public class AbiphoneWalter extends AbiphoneNPC {

	public AbiphoneWalter() {
		super("walter", "Walter", "Sells Superboom TNT.");
	}

	@Override
	public void onCall(HypixelPlayer player) {

	}

	@Override
	public void onAdd(SkyBlockPlayer player, int slot) {
		if (!player.getToggles().get(DatapointToggles.Toggles.ToggleType.HAS_GIVEN_WALTER_CUBE)) {
			if (dialogue().isInDialogue(player)) return;
			dialogue().setDialogue(player, "abiphone").thenRun(() -> {
				NPCOption.sendOption(player, "walter", false, Collections.singletonList(new NPCOption.Option(
						"pay", // actual id from Hypixel
						NamedTextColor.GREEN,
						true,
						"DONATE CUBE",
						(p) -> {
							CollectionCategory category = CollectionCategories.getCategory(ItemType.SULPHUR);
							CollectionCategory.ItemCollection collection = category == null
									? null
									: category.getCollection(ItemType.SULPHUR);
							int collected = player.getCollection().get(ItemType.SULPHUR);

							if (!meetsCollectionRequirement(collected, collection, 7)
									|| !player.removeItemFromPlayer(ItemType.ENCHANTED_SULPHUR_CUBE, 1)) {
								dialogue().setDialogue(player, "donate_cube_no_requirements");
								return;
							}

							player.getToggles().set(DatapointToggles.Toggles.ToggleType.HAS_GIVEN_WALTER_CUBE, true);
							dialogue().setDialogue(player, "donate_cube");
							super.onAdd(player, slot);
						}
				)));
			});
			return;
		}
		super.onAdd(player, slot);
	}

	@Override
	public DialogueSet[] dialogues(HypixelPlayer player) {
		return new DialogueSet[]{
				DialogueSet.builder()
						.key("abiphone").lines( // when clicking with an Abiphone
								"My abiphone is for Platinum-level donors of the Walter cause only.",
								"You know these superbooms don't craft themselves right?",
								"You just need <3>Sulphur Collection 7 <f>and to then donate an Enchanted Sulphur Cube!" // then show "donate cube" option
						).build(),
				DialogueSet.builder()
						.key("donate_cube").lines( // when donating the cube with requirements met
								"Welcome to the Platinum club, high roller!",
								"Call me anytime!",
								"And before you ask... yes, I do try to commercialize all of my friendships."
						).build(),
				DialogueSet.builder()
						.key("donate_cube_no_requirements").lines( // when donating the cube without requirements met
								"Mmh... you're missing something to become a Platinum-level donor..."
						).build()
		};
	}

	@Override
	public ItemStack.Builder getIcon() {
		return ItemStacks.head("43ac21b653a27632dbc8373c6e6fba5b8c97b7ecbfef2d793630d149b116ba81", Text.empty(), List.of());
	}

	static boolean meetsCollectionRequirement(int collected, CollectionCategory.ItemCollection collection, int level) {
		if (collection == null) {
			return false;
		}
		CollectionCategory.ItemCollectionReward[] rewards = collection.rewards();
		if (level < 1 || level > rewards.length) {
			return false;
		}
		return collected >= rewards[level - 1].requirement();
	}
}
