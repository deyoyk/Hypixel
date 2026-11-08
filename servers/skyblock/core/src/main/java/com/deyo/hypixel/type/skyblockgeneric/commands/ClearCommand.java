package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.NonPlayerItemUpdater;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "cl clear",
        description = "Rid your inventory of useless items",
        usage = "/clear",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ClearCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getInventory().clear();
            MinecraftServer.getSchedulerManager().scheduleTask(() -> player.getInventory().setItemStack(8,
                    new NonPlayerItemUpdater(new SkyBlockItem(ItemType.SKYBLOCK_MENU).getItemStack())
                            .getUpdatedItem().build()), TaskSchedule.tick(1), TaskSchedule.stop());
            player.sendMessage("<a>Whoosh!");
        });
    }
}
