package com.deyo.hypixel.type.generic.command.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.gui.impl.GUIRankColor;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "rankcolour rankcolor", description = "Change your MVP+ rank color",
    usage = "/rankcolor", permission = Rank.DEFAULT, allowsConsole = false)
public class RankColorCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> ((HypixelPlayer) sender).openView(new GUIRankColor()));
    }
}
