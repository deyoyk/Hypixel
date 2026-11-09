package com.deyo.hypixel.type.replayviewer.item.impl;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.replayviewer.TypeReplayViewerLoader;
import com.deyo.hypixel.type.replayviewer.item.ReplayItem;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlaybackControlItem extends ReplayItem {

    public PlaybackControlItem() {
        super("playback");
    }

    @Override
    public ItemStack getBlandItem() {
        return ItemStacks.named(Material.MAGENTA_DYE, "<a>Click to Pause").build();
    }

    @Override
    public ItemStack getItemStack(HypixelPlayer... p) {
        HypixelPlayer player = p[0];
        AtomicBoolean isPlaying = new AtomicBoolean(true);
        TypeReplayViewerLoader.getSession(player).ifPresent(
            session -> isPlaying.set(session.isPlaying())
        );
        if (isPlaying.get()) {
            return appendData(ItemStacks.named(Material.MAGENTA_DYE, "<a>Click to Pause")).build();
        }
        return appendData(ItemStacks.item(Material.GRAY_DYE, """
                <a>Click to Resume
                <7>The replay is currently paused.""")).build();
    }

    @Override
    public void onItemInteract(PlayerInstanceEvent event) {
        if (event instanceof CancellableEvent cancellable) {
            cancellable.setCancelled(true);
        }

        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        TypeReplayViewerLoader.getSession(player).ifPresentOrElse(
            session -> {
                if (session.isPlaying()) {
                    session.pause();
                } else {
                    session.play();
                }
                TypeReplayViewerLoader.populateInventory(player);
            },
            () -> player.sendMessage("<c>No active replay session.")
        );
    }
}
