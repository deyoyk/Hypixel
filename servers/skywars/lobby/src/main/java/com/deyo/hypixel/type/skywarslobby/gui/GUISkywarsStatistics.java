package com.deyo.hypixel.type.skywarslobby.gui;

import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skywars.SkywarsLeaderboardMode;
import com.deyo.hypixel.commons.skywars.SkywarsLeaderboardPeriod;
import com.deyo.hypixel.commons.skywars.SkywarsModeStats;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointSkywarsKitStats;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointSkywarsModeStats;
import com.deyo.hypixel.type.generic.data.handlers.SkywarsDataHandler;
import com.deyo.hypixel.type.generic.gui.inventory.HypixelInventoryGUI;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIClickableItem;
import com.deyo.hypixel.type.generic.gui.inventory.item.GUIItem;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.Map;

public class GUISkywarsStatistics extends HypixelInventoryGUI {

    public GUISkywarsStatistics() {
        super("SkyWars Statistics", InventoryType.CHEST_6_ROW);
    }

    @Override
    public void onOpen(InventoryGUIOpenEvent e) {
        HypixelPlayer player = e.player();
        SkywarsDataHandler handler = SkywarsDataHandler.getUser(player);

        SkywarsModeStats stats;
        if (handler != null) {
            stats = handler.get(SkywarsDataHandler.Data.MODE_STATS, DatapointSkywarsModeStats.class).getValue();
        } else {
            stats = SkywarsModeStats.empty();
        }

        ChallengeStats challenges = handler == null
                ? ChallengeStats.EMPTY
                : challengeStats(handler.get(SkywarsDataHandler.Data.KIT_STATS,
                        DatapointSkywarsKitStats.class).getValue().getKitStats());

        set(new GUIItem(4) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getTotalWins(SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getTotalLosses(SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getTotalKills(SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getTotalAssists(SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getTotalDeaths(SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getTotalMeleeKills(SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getTotalBowKills(SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getTotalVoidKills(SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getTotalArrowsShot(SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getTotalArrowsHit(SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getTotalChestsOpened(SkywarsLeaderboardPeriod.LIFETIME);
                long souls = stats.getTotalSoulsGathered(SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getTotalHeads(SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStacks.item(Material.PAPER, 1, """
                        <a>All Mode Statistics
                        <7>Wins: <a>{:,}
                        <7>Losses: <a>{:,}

                        <7>Kills: <a>{:,}
                        <7>Assists: <a>{:,}
                        <7>Deaths: <a>{:,}

                        <7>Melee Kills: <a>{:,}
                        <7>Bow Kills: <a>{:,}
                        <7>Void Kills: <a>{:,}

                        <7>Arrows Shot: <a>{:,}
                        <7>Arrows Hit: <a>{:,}

                        <7>Chests Opened: <a>{:,}
                        <7>Enderpearls Thrown: <a>0
                        <7>Souls Gathered: <a>{:,}
                        <7>Heads: <a>{:,}""",
                        wins, losses, kills, assists, deaths, meleeKills, bowKills, voidKills,
                        arrowsShot, arrowsHit, chests, souls, heads);
            }
        });

        set(new GUIItem(18) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.PAPER, 1, """
                        <a>Mini Statistics
                        <7>Wins: <a>0

                        <7>Kills: <a>0
                        <7>Assists: <a>0

                        <7>Melee Kills: <a>0
                        <7>Bow Kills: <a>0
                        <7>Void Kills: <a>0

                        <7>Arrows Shot: <a>0
                        <7>Arrows Hit: <a>0

                        <7>Chests Opened: <a>0""");
            }
        });

        set(new GUIItem(20) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getWins(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getWins(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getLosses(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getLosses(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getAssists(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getAssists(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getDeaths(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getDeaths(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getMeleeKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getMeleeKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getBowKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getBowKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getVoidKills(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getVoidKills(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getArrowsShot(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getArrowsShot(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getArrowsHit(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getArrowsHit(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getChestsOpened(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getChestsOpened(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getHeads(SkywarsLeaderboardMode.SOLO_NORMAL, SkywarsLeaderboardPeriod.LIFETIME)
                        + stats.getHeads(SkywarsLeaderboardMode.SOLO_INSANE, SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStacks.item(Material.PAPER, 1, """
                        <a>Solo Statistics
                        <7>Wins: <a>{:,}
                        <7>Losses: <a>{:,}

                        <7>Kills: <a>{:,}
                        <7>Assists: <a>{:,}
                        <7>Deaths: <a>{:,}

                        <7>Melee Kills: <a>{:,}
                        <7>Bow Kills: <a>{:,}
                        <7>Void Kills: <a>{:,}

                        <7>Arrows Shot: <a>{:,}
                        <7>Arrows Hit: <a>{:,}

                        <7>Chests Opened: <a>{:,}

                        <7>Heads: <a>{:,}""",
                        wins, losses, kills, assists, deaths, meleeKills, bowKills, voidKills,
                        arrowsShot, arrowsHit, chests, heads);
            }
        });

        set(new GUIItem(22) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                long wins = stats.getWins(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long losses = stats.getLosses(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long kills = stats.getKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long assists = stats.getAssists(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long deaths = stats.getDeaths(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long meleeKills = stats.getMeleeKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long bowKills = stats.getBowKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long voidKills = stats.getVoidKills(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsShot = stats.getArrowsShot(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long arrowsHit = stats.getArrowsHit(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long chests = stats.getChestsOpened(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);
                long heads = stats.getHeads(SkywarsLeaderboardMode.DOUBLES, SkywarsLeaderboardPeriod.LIFETIME);

                return ItemStacks.item(Material.PAPER, 1, """
                        <a>Doubles Statistics
                        <7>Wins: <a>{:,}
                        <7>Losses: <a>{:,}

                        <7>Kills: <a>{:,}
                        <7>Assists: <a>{:,}
                        <7>Deaths: <a>{:,}

                        <7>Melee Kills: <a>{:,}
                        <7>Bow Kills: <a>{:,}
                        <7>Void Kills: <a>{:,}

                        <7>Arrows Shot: <a>{:,}
                        <7>Arrows Hit: <a>{:,}

                        <7>Chests Opened: <a>{:,}

                        <7>Heads: <a>{:,}""",
                        wins, losses, kills, assists, deaths, meleeKills, bowKills, voidKills,
                        arrowsShot, arrowsHit, chests, heads);
            }
        });

        set(new GUIItem(24) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.PAPER, 1, """
                        <a>Mega Statistics
                        <7>Wins: <a>0
                        <7>Losses: <a>0

                        <7>Kills: <a>0
                        <7>Assists: <a>0
                        <7>Deaths: <a>0

                        <7>Melee Kills: <a>0
                        <7>Bow Kills: <a>0
                        <7>Void Kills: <a>0

                        <7>Arrows Shot: <a>0
                        <7>Arrows Hit: <a>0

                        <7>Chests Opened: <a>0""");
            }
        });

        set(new GUIItem(26) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.PAPER, 1, """
                        <a>Ranked Statistics
                        <7>Wins: <a>0
                        <7>Losses: <a>0

                        <7>Kills: <a>0
                        <7>Assists: <a>0
                        <7>Deaths: <a>0

                        <7>Melee Kills: <a>0
                        <7>Bow Kills: <a>0
                        <7>Void Kills: <a>0

                        <7>Arrows Shot: <a>0
                        <7>Arrows Hit: <a>0

                        <7>Chests Opened: <a>0""");
            }
        });

        set(new GUIItem(30) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.BLAZE_POWDER, 1, """
                        <a>SkyWars Challenge Statistics
                        <7>Total Challenge Wins: <a>{}

                        <7>Archer Wins: <a>{}
                        <7>Half Health Wins: <a>{}
                        <7>No Block Wins: <a>{}
                        <7>No Chest Wins: <a>{}
                        <7>Paper Wins: <a>{}
                        <7>Rookie Wins: <a>{}
                        <7>UHC Wins: <a>{}
                        <7>Ultimate Warrior Wins: <a>{}""",
                        challenges.total(), challenges.archer(), challenges.halfHealth(),
                        challenges.noBlock(), challenges.noChest(), challenges.paper(),
                        challenges.rookie(), challenges.uhc(), challenges.ultimateWarrior());
            }
        });

        set(new GUIItem(32) {
            @Override
            public ItemStack.Builder getItem(HypixelPlayer player) {
                return ItemStacks.item(Material.BREWING_STAND, 1, """
                        <a>Lab Statistics
                        <7>Hunters vs Beasts Wins: <a>0
                        <7>Lucky Blocks Wins: <a>0
                        <7>TNT Madness Wins: <a>0
                        <7>Slime Wins: <a>0
                        <7>Rush Wins: <a>0""");
            }
        });

        set(GUIClickableItem.getCloseItem(49));
        updateItemStacks(getInventory(), getPlayer());
    }

    static ChallengeStats challengeStats(Map<String, DatapointSkywarsKitStats.KitStatistics> stats) {
        int archer = 0;
        int halfHealth = 0;
        int noBlock = 0;
        int noChest = 0;
        int paper = 0;
        int rookie = 0;
        int uhc = 0;
        int ultimateWarrior = 0;

        for (DatapointSkywarsKitStats.KitStatistics entry : stats.values()) {
            archer += entry.getArcherWins();
            halfHealth += entry.getHalfHealthWins();
            noBlock += entry.getNoBlockWins();
            noChest += entry.getNoChestWins();
            paper += entry.getPaperWins();
            rookie += entry.getRookieWins();
            uhc += entry.getUhcWins();
            ultimateWarrior += entry.getUltimateWarriorWins();
        }

        return new ChallengeStats(archer, halfHealth, noBlock, noChest, paper, rookie, uhc, ultimateWarrior);
    }

    record ChallengeStats(int archer, int halfHealth, int noBlock, int noChest,
                          int paper, int rookie, int uhc, int ultimateWarrior) {
        static final ChallengeStats EMPTY = new ChallengeStats(0, 0, 0, 0, 0, 0, 0, 0);

        int total() {
            return archer + halfHealth + noBlock + noChest + paper + rookie + uhc + ultimateWarrior;
        }
    }

    @Override
    public boolean allowHotkeying() {
        return false;
    }

    @Override
    public void onBottomClick(InventoryPreClickEvent e) {
        e.setCancelled(true);
    }
}
