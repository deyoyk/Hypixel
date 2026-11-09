package com.deyo.hypixel.type.replayviewer.item.impl;

import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlaybackControlItemTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void blandItemIsAlwaysAvailable() {
        PlaybackControlItem item = new PlaybackControlItem();

        assertNotNull(item.getBlandItem());
        assertEquals(Material.MAGENTA_DYE, item.getBlandItem().material());
    }

    @Test
    void blandItemKeepsThePlaybackId() {
        PlaybackControlItem item = new PlaybackControlItem();

        assertEquals("playback", item.getId());
        assertNotNull(item.appendData(item.getBlandItem().builder()).build());
    }
}
