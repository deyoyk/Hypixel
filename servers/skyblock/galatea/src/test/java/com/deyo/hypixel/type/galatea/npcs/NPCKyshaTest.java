package com.deyo.hypixel.type.galatea.npcs;

import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.hunting.GUIAttributeFusion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NPCKyshaTest {

    @Test
    void fusionGuiIsUsable() {
        GUIAttributeFusion gui = NPCKysha.fusionGui();

        assertNotNull(gui);
        assertEquals(1, gui.initialState().page());
    }
}
