package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.BestiaryCategories;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.BestiaryEntry;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.GUIBestiary;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.bestiary.GUIBestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(
        labels = "bestiary",
        description = "Opens the bestiary menu",
        usage = "/bestiary [mob]",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class BestiaryCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ((SkyBlockPlayer) sender).openView(new GUIBestiary());
        });

        ArgumentString mobArgument = new ArgumentString("mob");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String mobId = context.get(mobArgument);
            BestiaryCategories category = findCategory(mobId);

            if (category == null) {
                player.sendMessage("<c>Unknown bestiary mob: {}", mobId);
                return;
            }

            player.openView(new GUIBestiaryMob(category, findEntry(category, mobId)));
        }, mobArgument);
    }

    static BestiaryCategories findCategory(String mobId) {
        for (BestiaryCategories category : BestiaryCategories.values()) {
            if (findEntry(category, mobId) != null) {
                return category;
            }
        }
        return null;
    }

    static BestiaryEntry findEntry(BestiaryCategories category, String mobId) {
        for (BestiaryEntry entry : category.getEntries()) {
            for (var mob : entry.getMobs()) {
                if (mob.getMobID().equalsIgnoreCase(mobId)) {
                    return entry;
                }
            }
        }
        return null;
    }
}
