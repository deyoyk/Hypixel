package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.region;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.entity.drop.ItemDrops;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.DroppedItemEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.foraging.ForagingTreeManager;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.CustomDropComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.components.RegionSelectorComponent;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegenConfiguration;
import com.deyo.hypixel.type.skyblockgeneric.region.SkyBlockRegion;
import com.deyo.hypixel.type.skyblockgeneric.region.mining.MineableBlock;
import com.deyo.hypixel.type.skyblockgeneric.region.mining.handler.SkyBlockMiningHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

public class ActionRegionBlockBreak implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockBreakEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        final ItemStack stack = event.getPlayer().getItemInMainHand();
        final SkyBlockItem item = new SkyBlockItem(stack);

        if (item.hasComponent(RegionSelectorComponent.class)) {
            RegionSelectorComponent.SelectedRegion region = RegionSelectorComponent.getPlayerRegionSelection().get(player);
            if (region == null) {
                region = new RegionSelectorComponent.SelectedRegion(null, null);
            }
            region.setPos1(event.getBlockPosition());
            RegionSelectorComponent.getPlayerRegionSelection().put(player, region);
            player.sendMessage("<a>Position 1 set to <e>{}<a>.", event.getBlockPosition());
            event.setCancelled(true);
            return;
        }

        // Skip if player has build bypass
        if (player.isBypassBuild()) return;

        event.setCancelled(true);
        SkyBlockRegion region = SkyBlockRegion.getRegionOfPosition(event.getBlockPosition());

        Block block = event.getBlock();
        Material material = Material.fromKey(block.name());
        boolean shouldItemDrop = false;

        // Leaves on public foraging islands are authored parts of their tree. Handle them before
        // region mineability validation so players can remove them and the tree can restore them.
        if (!HypixelConst.isIslandServer() && ForagingTreeManager.isTreeLeaf(block)
                && ForagingTreeManager.breakLeaf(player, event.getBlockPosition())) {
            return;
        }

        // Handle island server block breaks
        if (HypixelConst.isIslandServer()) {
            event.getInstance().setBlock(event.getBlockPosition(), Block.AIR);
            shouldItemDrop = true;
        }

        // Handle region-specific block breaks
        else if (region != null) {
            RegionType type = region.getType();
            SkyBlockRegenConfiguration mining = type.getMiningHandler();

            // Validate if block can be mined in this region
            if (material != null && mining != null && !mining.getMineableBlocks(player.getInstance(), event.getBlockPosition()).contains(material)) {
                event.setCancelled(true);
                return;
            }

            // Check if player's tool can break this block using the handler system
            MineableBlock mineableBlock = MineableBlock.get(block);
            SkyBlockItem heldItem = new SkyBlockItem(player.getItemInMainHand());
            if (mineableBlock != null) {
                SkyBlockMiningHandler handler = mineableBlock.getMiningHandler();

                // Check if tool can break this block (unless it breaks instantly)
                if (!handler.breaksInstantly() && !handler.canToolBreak(heldItem)) {
                    event.setCancelled(true);
                    return;
                }

                // Check breaking power requirement
                int playerBreakingPower = heldItem.getAttributeHandler().getBreakingPower();
                if (playerBreakingPower < handler.getMiningPowerRequirement()) {
                    event.setCancelled(true);
                    return;
                }
            }

            // Authored Park/Galatea trees are handled as a single tracked structure so Sweep can
            // harvest connected logs and the exact original blocks can grow back bottom-up.
            if (ForagingTreeManager.isTreeLog(block)) {
                List<ForagingTreeManager.HarvestedLog> harvested = ForagingTreeManager.breakLogs(
                        player, event.getBlockPosition(), heldItem
                );
                for (ForagingTreeManager.HarvestedLog log : harvested) {
                    processTreeLog(player, heldItem, region, log);
                }
                return;
            }

            // Queue block for mining if in valid region
            if (mining != null && material != null) {
                mining.addToQueue(player, event.getBlockPosition().asPos(), (SharedInstance) player.getInstance());
                shouldItemDrop = true;
            }
        }
        // Cancel if not in a region at all
        else {
            event.setCancelled(true);
            return;
        }

        // Handle item drops for valid breaks
        if (material != null && shouldItemDrop) {
            SkyBlockItem brokenBlockItem = new SkyBlockItem(material);

            // Determine which item(s) should be dropped using CustomDropComponent
            List<SkyBlockItem> customDrops = CustomDropComponent.simulateDrop(
                    brokenBlockItem,
                    player,
                    new SkyBlockItem(player.getItemInMainHand()),
                    region,
                    HypixelConst.getTypeLoader().getType(),
                    HypixelConst.isIslandServer()
            );

            // Call custom break event
            boolean playerPlaced = block.hasTag(Tag.Boolean("player_placed")) && block.getTag(Tag.Boolean("player_placed"));

            HypixelEventHandler.callCustomEvent(new CustomBlockBreakEvent(
                    player, material, event.getBlockPosition(), customDrops, playerPlaced
            ));

            // Process each drop with fortune multiplier
            MineableBlock mineableBlock = MineableBlock.get(block);
            double dropMultiplier = 1;
            if (mineableBlock != null && !customDrops.isEmpty()) {
                try {
                    var statistics = player.getStatistics().allStatistics();
                    double baseFortune = statistics.getOverall(mineableBlock.getBlockType().baseSkillFortune());
                    double specificFortune = statistics.getOverall(mineableBlock.getBlockType().specificBlockFortune());
                    dropMultiplier = fortuneMultiplier(baseFortune, specificFortune);
                } catch (NullPointerException ignored) {
                }
            }

            for (SkyBlockItem dropItem : customDrops) {
                // Calculate drop amount with fortune multiplier
                int dropAmount = fortuneAmount(dropItem.getAmount(), dropMultiplier);

                // Update the drop item amount
                dropItem.setAmount(dropAmount);
                ItemType droppedItemType = dropItem.getAttributeHandler().getPotentialType();

                // Handle item distribution based on player conditions
                if (player.canInsertItemIntoSacks(droppedItemType, dropAmount)) {
                    player.getSackItems().increase(droppedItemType, dropAmount);
                } else if (player.getSkyBlockExperience().getLevel().asInt() >= 6) {
                    player.addAndUpdateItem(dropItem);
                } else {
                    // Determine nearest air block between ore and player
                    Pos orePos = event.getBlockPosition().asPos();
                    Pos playerPos = player.getPosition();

                    Pos[] offsets = {
                            new Pos(1, 0, 0), new Pos(-1, 0, 0),
                            new Pos(0, 1, 0), new Pos(0, -1, 0),
                            new Pos(0, 0, 1), new Pos(0, 0, -1)
                    };

                    Pos nearestAirBlock = null;
                    double closestDistanceSquared = Double.MAX_VALUE;

                    for (Pos offset : offsets) {
                        Pos adjacentPos = orePos.add(offset);
                        Block block2 = player.getInstance().getBlock(adjacentPos);

                        if (block2.air()) {
                            double distanceSquared = adjacentPos.distanceSquared(playerPos);
                            if (distanceSquared < closestDistanceSquared) {
                                closestDistanceSquared = distanceSquared;
                                nearestAirBlock = adjacentPos;
                            }
                        }
                    }

                    // Use the nearest air block or fallback to default position
                    Pos dropBlock = (nearestAirBlock != null) ? nearestAirBlock : orePos.add(0, 1, 0);

                    // Spawn the item
                    ItemDrops.dropFromBlock(new DroppedItemEntityImpl(dropItem, player),
                            player.getInstance(), dropBlock);
                }
            }
        }
    }

    static double fortuneMultiplier(double baseFortune, double specificFortune) {
        return 1 + ((baseFortune + specificFortune) * 0.01);
    }

    static int fortuneAmount(int baseAmount, double multiplier) {
        return (int) Math.ceil(baseAmount * multiplier);
    }

    private void processTreeLog(SkyBlockPlayer player, SkyBlockItem heldItem, SkyBlockRegion region,
                                ForagingTreeManager.HarvestedLog harvested) {        Material originalMaterial = Material.fromKey(harvested.originalBlock().key());
        if (originalMaterial == null) return;

        SkyBlockItem brokenItem = harvested.dropType() == null
                ? new SkyBlockItem(originalMaterial)
                : new SkyBlockItem(harvested.dropType());
        List<SkyBlockItem> drops = CustomDropComponent.simulateDrop(
                brokenItem,
                player,
                heldItem,
                region,
                HypixelConst.getTypeLoader().getType(),
                false
        );

        var statistics = player.getStatistics().allStatistics();
        double fortune = statistics.getOverall(
                com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic.FORAGING_FORTUNE
        );
        if (harvested.dropType() == ItemType.FIG_LOG) {
            fortune += statistics.getOverall(
                    com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic.FIG_FORTUNE
            );
        } else if (harvested.dropType() == ItemType.MANGROVE_LOG) {
            fortune += statistics.getOverall(
                    com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic.MANGROVE_FORTUNE
            );
        }

        double multiplier = 1 + fortune * 0.01;
        for (SkyBlockItem drop : drops) {
            drop.setAmount(fortuneAmount(drop.getAmount(), multiplier));
        }

        // Fire after fortune is applied so collection gains match the items actually awarded.
        Material harvestedMaterial = Material.fromKey(brokenItem.getMaterial().key());
        HypixelEventHandler.callCustomEvent(new CustomBlockBreakEvent(
                player,
                harvestedMaterial == null ? originalMaterial : harvestedMaterial,
                harvested.position(),
                drops,
                false
        ));

        for (SkyBlockItem drop : drops) {
            distributeDrop(player, drop, harvested.position().asPos());
        }
    }

    private void distributeDrop(SkyBlockPlayer player, SkyBlockItem dropItem, Pos source) {
        int amount = dropItem.getAmount();
        ItemType type = dropItem.getAttributeHandler().getPotentialType();
        if (player.canInsertItemIntoSacks(type, amount)) {
            player.getSackItems().increase(type, amount);
            return;
        }
        if (player.getSkyBlockExperience().getLevel().asInt() >= 6) {
            player.addAndUpdateItem(dropItem);
            return;
        }

        Pos[] offsets = {
                new Pos(1, 0, 0), new Pos(-1, 0, 0),
                new Pos(0, 1, 0), new Pos(0, -1, 0),
                new Pos(0, 0, 1), new Pos(0, 0, -1)
        };
        Pos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Pos offset : offsets) {
            Pos candidate = source.add(offset);
            if (!player.getInstance().getBlock(candidate).air()) continue;
            double distance = candidate.distanceSquared(player.getPosition());
            if (distance < nearestDistance) {
                nearest = candidate;
                nearestDistance = distance;
            }
        }

        Pos dropBlock = nearest == null ? source.add(0, 1, 0) : nearest;
        ItemDrops.dropFromBlock(new DroppedItemEntityImpl(dropItem, player), player.getInstance(), dropBlock);
    }
}
