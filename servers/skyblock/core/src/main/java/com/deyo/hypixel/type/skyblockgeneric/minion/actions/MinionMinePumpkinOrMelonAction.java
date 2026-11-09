package com.deyo.hypixel.type.skyblockgeneric.minion.actions;

import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class MinionMinePumpkinOrMelonAction extends MinionAction {
    private final Block toMine;
    private final MinionMineAction delegate;

    public MinionMinePumpkinOrMelonAction(Block toMine) {
        this.toMine = toMine;
        this.delegate = new MinionMineAction(toMine);
    }

    @Override
    public @NotNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        return delegate.onAction(event, minion, island);
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        return delegate.checkMaterials(minion, island);
    }
}
