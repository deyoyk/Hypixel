package com.deyo.hypixel.type.murdermysterylobby.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.VillagerConfiguration;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.gui.GUIGameQuests;

import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;

public class QuestMasterNPC extends HypixelNPC {

    public QuestMasterNPC() {
        super(new VillagerConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{
                        "<b>Quest Master",
                        "<e><l>RIGHT CLICK"
                };
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(12.5, 68, 8.5, 125, 0);
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
        new GUIGameQuests(AchievementCategory.MURDER_MYSTERY).open(event.player());
    }
}
