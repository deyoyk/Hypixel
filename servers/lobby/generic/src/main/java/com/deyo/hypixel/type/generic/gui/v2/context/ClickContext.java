package com.deyo.hypixel.type.generic.gui.v2.context;

import net.minestom.server.inventory.click.Click;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public record ClickContext<S>(
        int slot,
        Click click,
        HypixelPlayer player,
        S state
) {
}

