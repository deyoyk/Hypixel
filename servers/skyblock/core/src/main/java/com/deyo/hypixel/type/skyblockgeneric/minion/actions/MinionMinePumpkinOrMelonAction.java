package com.deyo.hypixel.type.skyblockgeneric.minion.actions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
@Getter
public class MinionMinePumpkinOrMelonAction extends MinionAction {
    private final Block toMine;

    @Override
    public @NotNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        return null;
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        return false;
    }
}
