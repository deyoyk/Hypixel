package com.deyo.hypixel.type.hub.npcs.election;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.entity.npc.configuration.HumanConfiguration;
import com.deyo.hypixel.type.generic.event.custom.NPCInteractEvent;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.skyblockgeneric.elections.ElectionManager;
import com.deyo.hypixel.type.skyblockgeneric.elections.SkyBlockMayor;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.election.MayorMenuView;

public abstract class AbstractCurrentMayorNPC extends HypixelNPC {

    protected AbstractCurrentMayorNPC(Pos mayorPosition) {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                if (mayor == null) return new String[]{
                    "<key:'npcs_hub.election.mayor_unknown'>",
                    "<key:'npcs_hub.election.click'>"
                };
                return new String[]{
                    Text.of("Mayor {}", mayor.getDisplayName()).serialize(),
                    "<key:'npcs_hub.election.click'>"
                };
            }

            @Override
            public String signature(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                return mayor != null ? mayor.getSignature() : "";
            }

            @Override
            public String texture(HypixelPlayer player) {
                SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
                return mayor != null ? mayor.getTexture() : "";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return mayorPosition;
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent event) {
        SkyBlockMayor mayor = ElectionManager.getCurrentMayor();
        if (mayor == null) {
            event.player().sendMessage(Text.key("npcs_hub.election.hello"));
            return;
        }
        event.player().openView(new MayorMenuView());
    }
}
