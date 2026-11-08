package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.data.crystals.CrystalCatalog;
import com.deyo.hypixel.type.skyblockgeneric.entity.ServerCrystalImpl;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.ServerOrbComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.SkullHeadComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "spawncrystal addcrystal",
        description = "Spawns in a crystal at the player's location.",
        usage = "/addcrystal",
        permission = Rank.STAFF,
        allowsConsole = false)
public class AddCrystalCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemType = new ArgumentEnum<>("itemType", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ItemType type = context.get(itemType);
            SkyBlockItem item = new SkyBlockItem(type);

            // Spawn the orb
            ServerOrbComponent asOrb = item.getComponent(ServerOrbComponent.class);
            ServerCrystalImpl crystal = new ServerCrystalImpl(
                    asOrb.getSpawnMaterialFunction(),
                    item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    asOrb.getValidBlocks()
            );
            crystal.setInstance(((SkyBlockPlayer) sender).getInstance(), ((SkyBlockPlayer) sender).getPosition());

            CrystalCatalog.addCrystal(item.getComponent(SkullHeadComponent.class).getSkullTexture(item),
                    ((SkyBlockPlayer) sender).getPosition(),
                    type);
        }, itemType);
    }
}
