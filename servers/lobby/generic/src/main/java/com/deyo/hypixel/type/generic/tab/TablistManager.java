package com.deyo.hypixel.type.generic.tab;

import net.kyori.adventure.text.Component;
import net.minestom.server.color.TeamColor;
import net.minestom.server.entity.GameMode;
import net.minestom.server.network.packet.server.play.PlayerInfoRemovePacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.color.TeamColor;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.text.HypixelTextRenderer;
import com.deyo.hypixel.type.generic.text.RenderContext;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class TablistManager {
    private static final Map<HypixelPlayer, PlayerTabCache> tablistEntries = new ConcurrentHashMap<>();

    private static final class PlayerTabCache {
        private final List<UUID> tabEntries = new ArrayList<>();
        private final Set<String> createdTeams = new HashSet<>();
        private List<TablistModule.TablistEntry> lastEntries = List.of();
    }

    public abstract List<TablistModule> getModules();

    public void deleteTablistEntries(HypixelPlayer player) {
        tablistEntries.remove(player);
    }

    public void nullifyCache(HypixelPlayer player) {
        PlayerTabCache cache = tablistEntries.get(player);
        if (cache != null && !cache.tabEntries.isEmpty()) {
            player.sendPacket(new PlayerInfoRemovePacket(cache.tabEntries));
            cache.tabEntries.clear();
        }
    }

    public void runScheduler(Scheduler scheduler) {
        scheduler.scheduleTask(() -> {
            HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
                PlayerTabCache cache = tablistEntries.computeIfAbsent(player, ignored -> new PlayerTabCache());

                List<TablistModule.TablistEntry> entries = new ArrayList<>();
                for (TablistModule module : getModules()) {
                    try {
                        entries.addAll(module.getEntries(player));
                    } catch (Exception exception) {
                        org.tinylog.Logger.error(exception, "Failed to build tablist entries for {}",
                                module.getClass().getSimpleName());
                    }
                }

                if (!entriesChanged(entries, cache.lastEntries)) {
                    return;
                }
                cache.lastEntries = List.copyOf(entries);

                if (!cache.tabEntries.isEmpty()) {
                    player.sendPacket(new PlayerInfoRemovePacket(cache.tabEntries));
                }
                cache.tabEntries.clear();

                AtomicInteger slot = new AtomicInteger(0);

                entries.forEach(entry -> {
                    int slotIndex = slot.getAndIncrement();
                    String teamName = getTeamName(slotIndex);
                    String fakeProfileName = getFakeProfileName(slotIndex);

                    List<PlayerInfoUpdatePacket.Property> properties = new ArrayList<>();
                    TablistSkin skin = entry.registry();
                    properties.add(new PlayerInfoUpdatePacket.Property(
                        "textures",
                        skin.getTexture(),
                        skin.getSignature()));

                    if (cache.createdTeams.add(teamName)) {
                        TeamsPacket teamPacket = new TeamsPacket(teamName, new TeamsPacket.CreateTeamAction(
                                new TeamsPacket.Settings(
                                        Component.text(teamName),
                                        Component.text(teamName),
                                        Component.empty(),
                                        TeamsPacket.NameTagVisibility.ALWAYS,
                                        TeamsPacket.CollisionRule.ALWAYS,
                                        TeamColor.RED,
                                        (byte) 0x01
                                ),
                            new ArrayList<>(Collections.singletonList(fakeProfileName))
                        ));

                        player.sendPacket(teamPacket);
                    }

                    UUID uuid = UUID.nameUUIDFromBytes((player.getUuid() + "#tab#" + slotIndex)
                        .getBytes(StandardCharsets.UTF_8));
                    cache.tabEntries.add(uuid);

                    player.sendPackets(
                        new PlayerInfoUpdatePacket(EnumSet.of(
                            PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                            PlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                            PlayerInfoUpdatePacket.Action.UPDATE_LISTED
                        ), Collections.singletonList(new PlayerInfoUpdatePacket.Entry(
                            uuid,
                            fakeProfileName,
                            properties,
                            true,
                            0,
                            GameMode.CREATIVE,
                                HypixelTextRenderer.render(entry.content().asComponent(), RenderContext.of(player)),
                            null,
                            1, true)))
                    );
                });
            });
        }, TaskSchedule.seconds(5), TaskSchedule.seconds(3), ExecutionType.TICK_END);
    }

    static boolean entriesChanged(List<TablistModule.TablistEntry> current,
                                  List<TablistModule.TablistEntry> previous) {
        return !current.equals(previous);
    }

    private static String getTeamName(int slotIndex) {
        return String.format(Locale.ROOT, "TAB%03d", slotIndex);
    }

    private static String getFakeProfileName(int slotIndex) {
        return "tab" + Integer.toString(slotIndex, 36);
    }

}
