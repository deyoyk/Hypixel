package com.deyo.hypixel.type.skyblockgeneric.minion.actions;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.instance.Instance;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemQuantifiable;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.loottable.SkyBlockLootTable;
import com.deyo.hypixel.type.skyblockgeneric.minion.IslandMinionData;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionAction;
import com.deyo.hypixel.type.skyblockgeneric.utility.DamageIndicator;
import com.deyo.hypixel.type.generic.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
public class MinionKillMobAction extends MinionAction {
    private static final Map<Class<?>, SkyBlockLootTable> LOOT_TABLES = new ConcurrentHashMap<>();
    private final Supplier<SkyBlockMob> mobSupplier;
    private final Class<? extends SkyBlockMob> mobClass;

    public MinionKillMobAction(Supplier<SkyBlockMob> mobSupplier) {
        this(mobSupplier.get().getClass(), mobSupplier);
    }

    public MinionKillMobAction(Class<? extends SkyBlockMob> mobClass, Supplier<SkyBlockMob> mobSupplier) {
        this.mobClass = mobClass;
        this.mobSupplier = mobSupplier;
    }

    @Override
    public @NotNull List<SkyBlockItem> onAction(MinionActionEvent event, IslandMinionData.IslandMinion minion, Instance island) {
        List<Pos> horizontalCheckPositions = MathUtility.getRangeExcludingSelf(
                minion.getPosition(), 2 + minion.getBonusRange()
        );
        List<Pos> verticalCheckPositions = new ArrayList<>();

        // Add vertical positions 2 up and 3 down
        for (Pos pos : horizontalCheckPositions) {
            verticalCheckPositions.add(pos.add(0, 1, 0));
            verticalCheckPositions.add(pos.add(0, 2, 0));

            verticalCheckPositions.add(pos);

            verticalCheckPositions.add(pos.add(0, -1, 0));
            verticalCheckPositions.add(pos.add(0, -2, 0));
            verticalCheckPositions.add(pos.add(0, -3, 0));
        }

        List<SkyBlockMob> mobs = new ArrayList<>();
        for (Pos pos : verticalCheckPositions) {
            mobs.addAll(SkyBlockMob.getMobFromFuzzyPosition(island, pos));
        }

        for (SkyBlockMob mob : mobs) {
            if (mob.getClass() == mobClass) {
                event.setToLook(mob.getPosition());

                event.setAction(() -> {
                    if (mob.isDead()) return;

                    DamageIndicator indicator = new DamageIndicator();
                    indicator.pos(mob.getPosition());
                    indicator.damage(mob.getHealth());
                    indicator.critical(false);
                    indicator.display(island);

                    mob.damage(new Damage(DamageType.GENERIC_KILL, null, minion.getMinionEntity(), null, 0));
                    mob.kill();
                });

                if (mob.getLootTable() == null) return new ArrayList<>();

                Map<ItemType, SkyBlockLootTable.LootRecord> loot = mob.getLootTable().runChancesNoPlayer();
                List<SkyBlockItem> items = new ArrayList<>();
                for (ItemType itemType : loot.keySet()) {
                    SkyBlockLootTable.LootRecord record = loot.get(itemType);
                    items.add(new SkyBlockItem(itemType, record.getAmount()));
                }
                return items;
            }
        }

        // We need to spawn the mob
        Collections.shuffle(verticalCheckPositions);
        Pos positionToSpawn = null;
        for (Pos pos : verticalCheckPositions) {
            if (island.getBlock(pos).air() && island.getBlock(pos.add(0, 1, 0)).air()) {
                positionToSpawn = pos;
                break;
            }
        }

        if (positionToSpawn == null) throw new RuntimeException("No position to spawn mob");

        event.setToLook(positionToSpawn);
        Pos finalPositionToSpawn = positionToSpawn;
        event.setAction(() -> {
            SkyBlockMob mob = mobSupplier.get();
            mob.setInstance(island, finalPositionToSpawn.add(0.5, 0, 0.5));
        });

        return new ArrayList<>();
    }

    @Override
    public boolean checkMaterials(IslandMinionData.IslandMinion minion, Instance island) {
        SkyBlockLootTable lootTable = LOOT_TABLES.computeIfAbsent(mobClass, ignored -> {
            SkyBlockLootTable table = mobSupplier.get().getLootTable();
            if (table == null) throw new RuntimeException("Mob has no loot table");
            return table;
        });
        List<ItemType> lootTableItems = lootTable.getLootTableItems();
        List<ItemQuantifiable> itemsInMinion = minion.getItemsInMinion();

        if (itemsInMinion.stream().noneMatch(item -> {
            return lootTableItems.contains(item.getItem().getAttributeHandler().getPotentialType());
        })) {
            return true; // No items in minion that are in the loot table
        }

        boolean shouldAllow = false;
        for (ItemQuantifiable item : itemsInMinion) {
            ItemType itemType = item.getItem().getAttributeHandler().getPotentialType();
            if (lootTableItems.contains(itemType)) {
                if (item.getAmount() != 64) {
                    shouldAllow = true;
                }
            }
        }

        return !shouldAllow;
    }
}
