package com.deyo.hypixel.clientmod.commands;

import com.deyo.hypixel.clientmod.features.SpraySchemaRecorder;
import com.deyo.hypixel.clientmod.utils.ChatUtils;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;

public final class SpraySchemaRecorderCommand {
    private SpraySchemaRecorderCommand() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            RecreationCommand.register(dispatcher, ClientCommands.literal("listensprays")
                    .executes(_ -> {
                        SpraySchemaRecorder.start();
                        return 1;
                    }));

            RecreationCommand.register(dispatcher, ClientCommands.literal("listenspraysstop")
                    .executes(_ -> {
                        if (!SpraySchemaRecorder.isListening()) {
                            ChatUtils.warn("Spray listener is not running.");
                            return 1;
                        }
                        SpraySchemaRecorder.stop();
                        return 1;
                    }));
        });
    }
}
