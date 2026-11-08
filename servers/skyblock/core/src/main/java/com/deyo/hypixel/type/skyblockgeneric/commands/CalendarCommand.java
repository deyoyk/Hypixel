package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.calendar.GUICalendar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Opens the SkyBlock Calendar",
    usage = "/calendar",
    permission = Rank.DEFAULT,
    labels = "calendar",
    allowsConsole = false)
public class CalendarCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, _) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).openView(new GUICalendar());
        });
    }
}
