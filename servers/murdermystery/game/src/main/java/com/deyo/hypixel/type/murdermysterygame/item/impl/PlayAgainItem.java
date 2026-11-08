package com.deyo.hypixel.type.murdermysterygame.item.impl;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.ChooseGameProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetServerForMapProtocol;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.murdermysterygame.TypeMurderMysteryGameLoader;
import com.deyo.hypixel.type.murdermysterygame.game.Game;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.murdermysterygame.item.SimpleInteractableItem;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;

public class PlayAgainItem extends SimpleInteractableItem {
    private static final ProxyService PROXY_SERVICE = new ProxyService(ServiceType.ORCHESTRATOR);

    public PlayAgainItem() {
        super("play_again");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.PAPER, """
                <a>Play Again <7>(Right Click)
                <7>Right-click to queue for another game!""").build();
    }

    @Override
    public void onItemUse(PlayerUseItemEvent event) {
        MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();
        Game game = TypeMurderMysteryGameLoader.getPlayerGame(player);
        if (game != null) {
            String gameType = game.getGameType().toString();
            game.leave(player);

            // Queue for new game using ProxyService directly
            GetServerForMapProtocol.GetServerForMapMessage message =
                new GetServerForMapProtocol.GetServerForMapMessage(
                    ServerType.MURDER_MYSTERY_GAME,
                    null,
                    gameType,
                    1
                );

            PROXY_SERVICE.handleRequest(message).thenAccept(response -> {
                if (response instanceof GetServerForMapProtocol.GetServerForMapResponse resp) {
                    if (resp.server() != null) {
                        ChooseGameProtocol.ChooseGameMessage chooseMessage =
                            new ChooseGameProtocol.ChooseGameMessage(
                                player.getUuid(),
                                resp.server(),
                                resp.gameId()
                            );

                        PROXY_SERVICE.handleRequest(chooseMessage).thenRun(() -> {
                            player.asProxyPlayer().transferToWithIndication(resp.server().uuid());
                        });
                    }
                }
            });
        }
    }
}
