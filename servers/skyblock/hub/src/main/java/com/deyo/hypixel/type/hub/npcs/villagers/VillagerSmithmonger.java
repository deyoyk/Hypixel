package com.deyo.hypixel.type.hub.npcs.villagers;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.VillagerProfession;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.VillagerConfiguration;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class VillagerSmithmonger extends HypixelNPC {
    public VillagerSmithmonger() {
        super(new VillagerConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"<f>Smithmonger", "<e><l>CLICK"};
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(15.5, 63, -135, 0, 0);
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
        e.player().notImplemented();
    }
}
