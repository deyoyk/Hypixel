package com.deyo.hypixel.type.island.lifecycle;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionAction;
import com.deyo.hypixel.type.skyblockgeneric.minion.SkyBlockMinion;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleContext;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecyclePhase;
import com.deyo.hypixel.type.skyblockgeneric.user.island.IslandLifecycleStep;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public class MinionLoadStep implements IslandLifecycleStep {
    @Override
    public IslandLifecyclePhase phase() {
        return IslandLifecyclePhase.LOAD;
    }

    @Override
    public int order() {
        return 10;
    }

    @Override
    public void run(IslandLifecycleContext context) {
        Document document = context.island().getDatabase().getDocument();
        Map<String, Object> rawData = document == null ? null : (Map<String, Object>) document.get("minions");

        IslandMinionData minionData = rawData == null
                ? new IslandMinionData(context.island())
                : IslandMinionData.deserialize(rawData, context.island());

        context.island().setMinionData(minionData);

        long lastSaved = context.island().getLastSaved();
        long currentTime = System.currentTimeMillis();

        minionData.getMinions().forEach(data -> {
            int tierIndex = data.getTier();
            SkyBlockMinion.MinionTier tier = data.getMinion().asSkyBlockMinion().getTiers().get(tierIndex - 1);

            long timeBetweenActions = (long) tier.timeBetweenActions();
            double percentageSpeedIncrease = data.getSpeedPercentage();

            timeBetweenActions = (long) (timeBetweenActions / (1 + (percentageSpeedIncrease / 100)));

            int amountOfActions = Math.round((float) (currentTime - lastSaved) / (timeBetweenActions * 1000L));

            data.spawnMinion(context.island().getIslandInstance());
            if (lastSaved != 0) {
                Thread.startVirtualThread(() -> {
                    for (int i = 0; i < amountOfActions; i++) {
                        MinionAction action = data.getMinion().asSkyBlockMinion().getAction();
                        List<SkyBlockItem> items = action.onAction(
                                new MinionAction.MinionActionEvent(),
                                data,
                                context.island().getIslandInstance());

                        if (!items.isEmpty()) {
                            MinionAction.onMinionIteration(data, data.getMinionEntity().getMinion(), items);
                        }
                    }
                });
            }
        });
    }
}
