package com.deyo.hypixel.type.lobby.item.impl;

import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.lobby.gui.GUILobbySelector;
import com.deyo.hypixel.type.lobby.item.LobbyItem;

import java.util.function.Consumer;

public class LobbySelector extends LobbyItem {

    private final Consumer<HypixelPlayer> onInteract;

    public LobbySelector(Consumer<HypixelPlayer> onInteract) {
        super("lobby_selector");
        this.onInteract = onInteract;
    }

    public LobbySelector() {
        this(player -> {
            ServerType currentType = HypixelConst.getTypeLoader().getType();
            String lobbyName = StringUtility.toNormalCase(currentType.name());
            player.openView(new GUILobbySelector(currentType, lobbyName));
        });
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.item(Material.NETHER_STAR, """
                <a>Lobby Selector <7>(Right Click)
                <7>Right-click to switch between different lobbies!
                <7>Use this to stay with your friends.""").build();
    }

    @Override
    public void onItemDrop(ItemDropEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (event instanceof CancellableEvent cancellable) {
            cancellable.setCancelled(true);
        }
        onInteract.accept((HypixelPlayer) event.getPlayer());
    }
}
