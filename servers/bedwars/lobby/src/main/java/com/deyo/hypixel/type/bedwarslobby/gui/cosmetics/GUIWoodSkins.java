package com.deyo.hypixel.type.bedwarslobby.gui.cosmetics;

import com.deyo.hypixel.type.generic.collectibles.CollectibleCategory;
import com.deyo.hypixel.type.generic.collectibles.CollectibleDefinition;
import com.deyo.hypixel.type.generic.collectibles.CollectibleSelectionCheck;
import com.deyo.hypixel.type.generic.collectibles.bedwars.BedWarsCollectibleCatalog;
import com.deyo.hypixel.type.generic.collectibles.bedwars.BedWarsCollectibleStateService;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLeaderboardLong;
import com.deyo.hypixel.type.generic.data.handlers.BedWarsDataHandler;
import com.deyo.hypixel.type.generic.gui.impl.collectibles.CollectibleSelectionView;
import com.deyo.hypixel.type.generic.gui.v2.context.ViewContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.List;

public class GUIWoodSkins extends CollectibleSelectionView {

    public GUIWoodSkins() {
        super("Wood Skins ");
    }

    @Override
    protected List<CollectibleDefinition> loadItems(HypixelPlayer player) {
        BedWarsCollectibleCatalog.initialize();
        BedWarsCollectibleStateService.reconcileSelected(player, CollectibleCategory.WOOD_SKINS);
        return BedWarsCollectibleCatalog.getCategoryItems(CollectibleCategory.WOOD_SKINS);
    }

    @Override
    protected CollectibleSelectionCheck selectionCheck(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.checkSelectable(player, definition);
    }

    @Override
    protected boolean isSelected(HypixelPlayer player, CollectibleDefinition definition) {
        return BedWarsCollectibleStateService.isSelected(player, CollectibleCategory.WOOD_SKINS, definition.id());
    }

    @Override
    protected SelectionOutcome selectItem(HypixelPlayer player, ViewContext ctx, CollectibleDefinition definition) {
        BedWarsCollectibleStateService.SelectionResult result = BedWarsCollectibleStateService.select(player, definition);
        return result.success()
            ? SelectionOutcome.success(result.message())
            : SelectionOutcome.failure(result.message());
    }

    @Override
    protected boolean isPinnedDefault(HypixelPlayer player, CollectibleDefinition definition) {
        String pinned = BedWarsCollectibleCatalog.pinnedDefaultId(CollectibleCategory.WOOD_SKINS);
        return pinned != null && pinned.equals(definition.id());
    }

    @Override
    protected long tokenBalance(HypixelPlayer player) {
        BedWarsDataHandler dataHandler = BedWarsDataHandler.getUser(player);
        if (dataHandler == null) {
            return 0L;
        }
        return dataHandler.get(BedWarsDataHandler.Data.TOKENS, DatapointLeaderboardLong.class).getValue();
    }
}
