package com.deyo.hypixel.clientmod.mixin;

import com.deyo.hypixel.clientmod.features.fullcapture.FullCapture;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonPacketListenerImpl.class)
public abstract class ClientCommonPacketListenerImplMixin {
    @Inject(method = "send", at = @At("HEAD"))
    private void deyo-hypixel-clientmod$recordOutboundPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        FullCapture.recordOutbound(packet);
    }
}
