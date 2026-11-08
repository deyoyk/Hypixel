package com.deyo.hypixel.anticheat.loader;

import lombok.Getter;
import lombok.Setter;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;

import java.util.UUID;

@Getter
@Setter
public abstract class LoaderPacketHandler<T> {
    public abstract DeyoPacket buildDeyoPacket(UUID uuid, T packet);
    public abstract T buildLoaderPacket(UUID uuid, DeyoPacket packet);
    public abstract Class<T> getHandledPacketClass();
}
