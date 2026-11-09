package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.bedwars.BedWarsGameType;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.game.LobbyOrchestratorConnector;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class PlayAgainItem extends SimpleInteractableItem {

    public PlayAgainItem() {
        super("play_again");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.COMPASS, """
                <b><l>Play Again </l><7>(Right Click)
                <7>Right-click to play another game!""").build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (!(event.getPlayer() instanceof BedWarsPlayer player)) return;

        BedWarsGame game = player.getGame();
        if (game == null) {
            player.sendTo(ServerType.BEDWARS_LOBBY);
            return;
        }

        RequeueTarget target = requeueTarget(game.getGameType(), game.getMapEntry().getName());
        new LobbyOrchestratorConnector(player).sendToGame(target.serverType(), target.gameType(), target.mapName());
    }

    record RequeueTarget(ServerType serverType, String gameType, String mapName) {
    }

    static RequeueTarget requeueTarget(BedWarsGameType gameType, String mapName) {
        return new RequeueTarget(ServerType.BEDWARS_GAME, gameType.name(), mapName);
    }
}
