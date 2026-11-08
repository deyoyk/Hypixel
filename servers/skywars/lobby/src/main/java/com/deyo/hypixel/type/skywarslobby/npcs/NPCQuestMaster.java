package com.deyo.hypixel.type.skywarslobby.npcs;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.VillagerConfiguration;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.gui.GUIGameQuests;

import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;

public class NPCQuestMaster extends HypixelNPC {

	public NPCQuestMaster() {
		super(new VillagerConfiguration() {
			@Override
			public String[] holograms(HypixelPlayer player) {
				return new String[]{
						"<b>Quest Master",
						"<e><l>RIGHT CLICK",
				};
			}


			@Override
			public Pos position(HypixelPlayer player) {
				return new Pos(11.5, 63, 6.5, 110, 0);
			}

			@Override
			public boolean looking(HypixelPlayer player) {
				return false;
			}

			@Override
			public VillagerProfession profession() {
				return VillagerProfession.LIBRARIAN;
			}
		});
	}

	@Override
	public void onClick(NPCInteractEvent event) {
		new GUIGameQuests(AchievementCategory.SKYWARS).open(event.player());
	}
}
