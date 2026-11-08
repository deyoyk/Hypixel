package com.deyo.hypixel.type.skyblockgeneric.data;

import lombok.SneakyThrows;
import net.minestom.server.entity.Player;
import com.deyo.hypixel.commons.protocol.Serializer;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;

public abstract class SkyBlockDatapoint<T> extends Datapoint<T> {

    protected SkyBlockDatapoint(String key, T value, Serializer<T> serializer) {
        super(key, value, serializer);
    }

    @SneakyThrows
    public void setValueBypassCoop(T value) {
        setValueBypassOnChange(value);
    }

    @Override
    protected boolean hasOnChange() {
        if (data instanceof HypixelDataHandler.Data d) return d.onChange != null;
        if (data instanceof SkyBlockDataHandler.Data d) return d.onChange != null;
        return false;
    }

    @Override
    protected void triggerOnChange(Player player, Datapoint<?> datapoint) {
        if (data instanceof HypixelDataHandler.Data hypixelData && hypixelData.onChange != null) {
            hypixelData.onChange.accept(player, datapoint);
        } else if (data instanceof SkyBlockDataHandler.Data sbData && sbData.onChange != null) {
            sbData.onChange.accept(player, datapoint);
        }
    }
}
