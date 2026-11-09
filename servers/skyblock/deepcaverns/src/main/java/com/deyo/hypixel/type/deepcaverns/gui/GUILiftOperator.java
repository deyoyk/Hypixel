package com.deyo.hypixel.type.deepcaverns.gui;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointStringList;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;
import java.util.function.Consumer;

public class GUILiftOperator extends HypixelInventoryGUI {
	public GUILiftOperator() {
		super("Lift", InventoryType.CHEST_6_ROW);
	}

	@Override
	public boolean allowHotkeying() {
		return false;
	}

	@Override
	public void onOpen(InventoryGUIOpenEvent event) {
		fill(ItemStacks.filler(Material.BLACK_STAINED_GLASS_PANE));

		for (LiftLocation location : LiftLocation.values()) {
			set(new GUIClickableItem(location.slot) {
				@Override
				public void run(InventoryPreClickEvent e, HypixelPlayer p) {
					SkyBlockPlayer player = (SkyBlockPlayer) p;
					SkyBlockRegion region = player.getRegion();
					if (region != null) {
						if (region.getType().name().equals(location.name())) {
							p.sendMessage("<c>You are already in the {}!", location.prettyName());
							return;
						}
					}
					DatapointStringList discoveredZones = player.getSkyblockDataHandler().get(
							SkyBlockDataHandler.Data.VISITED_REGIONS,
							DatapointStringList.class
					);
					List<String> discoveredZonesList = discoveredZones.getValue();
					if (!discoveredZonesList.contains(location.prettyName()) && location != LiftLocation.DWARVEN_MINES) {
						player.sendMessage("<c>You have not discovered the {} yet!", location.prettyName());
						return;
					}
					location.consumer.accept(p);
					p.closeInventory();
				}

				@Override
				public ItemStack.Builder getItem(HypixelPlayer p) {
					SkyBlockPlayer player = (SkyBlockPlayer) p;
					DatapointStringList discoveredZones = player.getSkyblockDataHandler().get(
							SkyBlockDataHandler.Data.VISITED_REGIONS,
							DatapointStringList.class
					);
					List<String> discoveredZonesList = discoveredZones.getValue();
					if (!discoveredZonesList.contains(location.name()) && location != LiftLocation.DWARVEN_MINES) {
						return ItemStacks.item(location.material, """
								<a>{}
								<7>Click to teleport to the
								<b>{}<7>!

								<c>Not discovered yet!""", location.prettyName(), location.prettyName());
					}
					return ItemStacks.item(location.material, """
							<a>{}
							<7>Click to teleport to the
							<b>{}<7>!

							<e>Click to travel!""", location.prettyName(), location.prettyName());
				}
			});
		}

		updateItemStacks(getInventory(), getPlayer());
	}

	@Override
	public void onBottomClick(InventoryPreClickEvent e) {
		e.setCancelled(true);
	}

	enum LiftLocation {
		GUNPOWDER_MINES(Material.GOLD_INGOT, 10, (player) -> teleportLocation(player, new Pos(52.5, 150, 15.5, 90f, 0))),
		LAPIS_QUARRY(Material.LAPIS_LAZULI, 12, (player) -> teleportLocation(player, new Pos(52.5, 121, 15.5, 90f, 0))),
		PIGMENS_DEN(Material.REDSTONE, 14, (player) -> teleportLocation(player, new Pos(52.5, 101, 15.5, 90f, 0))),
		SLIMEHILL(Material.EMERALD, 16, (player) -> teleportLocation(player, new Pos(52.5, 66, 15.5, 90f, 0))),
		DIAMOND_RESERVE(Material.DIAMOND, 29, (player) -> teleportLocation(player, new Pos(52.5, 38, 15.5, 90f, 0))),
		OBSIDIAN_SANCTUARY(Material.OBSIDIAN, 31, (player) -> teleportLocation(player, new Pos(52.5, 13, 15.5, 90f, 0))),
		DWARVEN_MINES(Material.PRISMARINE, 33, (player) -> {
			player.sendTo(ServerType.SKYBLOCK_DWARVEN_MINES);
		});

		private final Material material;
		private final int slot;
		private final Consumer<HypixelPlayer> consumer;

		LiftLocation(Material material, int slot, Consumer<HypixelPlayer> consumer) {
			this.material = material;
			this.slot = slot;
			this.consumer = consumer;
		}

		private static void teleportLocation(HypixelPlayer player, Pos pos) {
			player.teleport(pos);
		}

		public String prettyName() {
			String lower = this.name().toLowerCase();
			String[] parts = lower.split("_");
			StringBuilder capitalized = new StringBuilder();
			for (String part : parts) {
				capitalized.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1)).append(" ");
			}
			return capitalized.toString().trim();
		}
	}
}
