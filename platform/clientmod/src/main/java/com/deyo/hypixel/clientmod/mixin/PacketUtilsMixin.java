package com.deyo.hypixel.clientmod.mixin;

import com.deyo.hypixel.clientmod.features.fullcapture.FullCapture;
import com.deyo.hypixel.clientmod.features.hudcapture.HudCaptureRecorder;
import com.deyo.hypixel.clientmod.features.packetlog.EntityPacketLogger;
import com.deyo.hypixel.clientmod.features.packetlog.RavengardSessionLogger;
import net.minecraft.network.PacketListener;
import net.minecraft.network.PacketProcessor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketUtils.class)
public abstract class PacketUtilsMixin {
    @Inject(
            method = "ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
            at = @At("HEAD")
    )
    private static <T extends PacketListener> void recordInboundPacket(
            Packet<T> packet,
            T listener,
            PacketProcessor engine,
            CallbackInfo callbackInfo
    ) {
        if (listener instanceof ClientGamePacketListener) {
            EntityPacketLogger.record(packet);
            RavengardSessionLogger.record(packet);
            HudCaptureRecorder.record(packet);
            FullCapture.recordInbound(packet);
        }
    }
}
