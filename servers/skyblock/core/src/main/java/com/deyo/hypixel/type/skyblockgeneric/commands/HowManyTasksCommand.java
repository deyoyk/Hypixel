package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.MinecraftServer;
import org.tinylog.Logger;
import net.minestom.server.timer.SchedulerManager;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;

import java.util.concurrent.atomic.AtomicInteger;

@CommandParameters(labels = "taskcount",
        description = "Gets the number of tasks currently running on the server",
        usage = "/taskcount",
        permission = Rank.STAFF,
        allowsConsole = false)
public class HowManyTasksCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SchedulerManager manager = MinecraftServer.getSchedulerManager();
            AtomicInteger TASK_COUNTER;

            // Use reflection to get the TASK_COUNTER field
            try {
                TASK_COUNTER = (AtomicInteger) manager.getClass().getDeclaredField("TASK_COUNTER").get(manager);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                Logger.error(e, "Error while counting tasks");
                return;
            }

            sender.sendMessage(Text.of("{} tasks running", TASK_COUNTER.get()));
        });
    }
}
