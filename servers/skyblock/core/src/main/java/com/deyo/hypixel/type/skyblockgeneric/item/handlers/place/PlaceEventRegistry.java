package com.deyo.hypixel.type.skyblockgeneric.item.handlers.place;

import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMinionData;
import com.deyo.hypixel.type.skyblockgeneric.item.components.MinionComponent;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;

import java.util.HashMap;
import java.util.Map;

public class PlaceEventRegistry {
    private static final Map<String, PlaceEventHandler> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("minion", (event, player, item) -> {
            if (!HypixelConst.isIslandServer()) {
                player.sendMessage("<c>You can only place minions on your island!");
                event.setCancelled(true);
                return;
            }

            DatapointMinionData.ProfileMinionData playerData = player.getSkyblockDataHandler().get(
                    SkyBlockDataHandler.Data.MINION_DATA, DatapointMinionData.class
            ).getValue();

            int slots = playerData.getSlots();

            IslandMinionData minionData = player.getSkyBlockIsland().getMinionData();

            if (minionData.getMinions().size() >= slots) {
                player.sendMessage("<c>You have reached the maximum amount of minions you can place!");
                event.setCancelled(true);
                return;
            }

            MinionComponent component = item.toConfigurableItem().getComponent(MinionComponent.class);

            IslandMinionData.IslandMinion minion = minionData.initializeMinion(event.getBlockPosition().asPos(),
                    component.getMinionRegistry(),
                    item.getAttributeHandler().getMinionData(),
                    item.getAttributeHandler().isMithrilInfused());
            minionData.spawn(minion);

            event.setBlock(Block.AIR);

            player.sendMessage("<b>You placed a minion! ({}/{})",
                    minionData.getMinions().size(), playerData.getSlots());
        });
        register("CARROT_CRYSTAL_PLACE", (event, player, item) -> {

        });
    }

    public static void register(String id, PlaceEventHandler handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static PlaceEventHandler getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}