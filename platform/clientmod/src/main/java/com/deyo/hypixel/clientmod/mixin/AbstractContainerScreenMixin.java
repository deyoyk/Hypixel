package com.deyo.hypixel.clientmod.mixin;

import com.deyo.hypixel.clientmod.features.SpraySchemaRecorder;
import com.deyo.hypixel.clientmod.features.fullcapture.FullCapture;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(method = "mouseClicked", at = @At("HEAD"))
    private void deyo-hypixel-clientmod$captureSprayRightClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if (event.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            SpraySchemaRecorder.handleContainerRightClick((AbstractContainerScreen<?>) (Object) this);
        }
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void deyo-hypixel-clientmod$captureSlotClick(Slot slot, int slotId, int buttonNum,
                                                          ContainerInput containerInput, CallbackInfo callbackInfo) {
        FullCapture.onSlotClick((AbstractContainerScreen<?>) (Object) this, slot, slotId, buttonNum, containerInput);
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void deyo-hypixel-clientmod$captureScreenClose(CallbackInfo callbackInfo) {
        FullCapture.onScreenClosed();
    }
}
