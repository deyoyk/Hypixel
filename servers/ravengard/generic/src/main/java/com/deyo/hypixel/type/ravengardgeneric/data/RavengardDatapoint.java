package com.deyo.hypixel.type.ravengardgeneric.data;

import net.minestom.server.entity.Player;
import com.deyo.hypixel.commons.protocol.Serializer;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public abstract class RavengardDatapoint<T> extends Datapoint<T> {

    protected RavengardDatapoint(String key, T value, Serializer<T> serializer) {
        super(key, value, serializer);
    }

    @Override
    protected boolean hasOnChange() {
        if (data instanceof HypixelDataHandler.Data hypixelData) {
            return hypixelData.onChange != null;
        }
        if (data instanceof RavengardDataHandler.Data ravengardData) {
            return ravengardData.onChange != null;
        }
        return false;
    }

    @Override
    protected void triggerOnChange(Player player, Datapoint<?> datapoint) {
        if (data instanceof HypixelDataHandler.Data hypixelData && hypixelData.onChange != null) {
            hypixelData.onChange.accept(player, datapoint);
        } else if (data instanceof RavengardDataHandler.Data ravengardData && ravengardData.onChange != null) {
            ravengardData.onChange.accept((HypixelPlayer) player, datapoint);
        }
    }
}
