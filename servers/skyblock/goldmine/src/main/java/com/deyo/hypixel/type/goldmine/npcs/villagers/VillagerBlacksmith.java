package com.deyo.hypixel.type.goldmine.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.VillagerConfiguration;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUIReforge;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;

public class VillagerBlacksmith extends HypixelNPC {
	public VillagerBlacksmith() {
		super(new VillagerConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{"Blacksmith", "<e><l>CLICK"};
			}

			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(-39.50, 77.00, -299.50, -125f, 0f);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return true;
			}

			@Override
			public VillagerProfession profession() {
				return VillagerProfession.WEAPONSMITH;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent e) {
		SkyBlockPlayer player = (SkyBlockPlayer) e.player();
		new GUIReforge().open(player);
	}
}