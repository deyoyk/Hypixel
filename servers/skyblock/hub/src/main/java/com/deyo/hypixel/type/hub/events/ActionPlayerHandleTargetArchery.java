package com.deyo.hypixel.type.hub.events;

import net.kyori.adventure.title.Title;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.tag.Tag;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.utility.TimedActivityHandler;
import com.deyo.hypixel.type.skyblockgeneric.targetpractice.PracticeTargets;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointArcheryPractice;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.ArrowHitBlockEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.BowComponent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockActionBar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.time.Duration;
import java.util.Map;

public class ActionPlayerHandleTargetArchery implements HypixelEventClass {
    private static final Pos ARCHERY_POSITION = DatapointArcheryPractice.getArcheryPosition();
    private static final int TOTAL_TARGETS = 16;

    public ActionPlayerHandleTargetArchery() {}

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void handleOnJoin(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!event.isFirstSpawn()) return;

        player.getArcheryPracticeData().initializeHologram(player);
    }


    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void handleOnArrowHit(ArrowHitBlockEvent event) {
        if (!event.isSkyBlockPlayer()) return;
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getShooter();
        TimedActivityHandler<?> handler = TimedActivityHandler.getActiveHandler(player);
        if (handler == null || !handler.isActive()) return;

        Block hitBlock = event.getHitBlock();
        Point hitPosition = event.getHitPosition();

        PracticeTargets target = PracticeTargets.getFromPosition(hitPosition.asPos());
        if (target == null) return;

        DatapointArcheryPractice.ArcheryPracticeData data = player.getArcheryPracticeData();
        if (data.hasHitTarget(target)) return;

        Block lantern = Block.LANTERN.withTag(Tag.Boolean("lit"), true);
        player.getInstance().setBlock(hitPosition, lantern);
        data.incrementTargetsHit(target);

        if (data.getTargetsHitList().size() >= TOTAL_TARGETS) {
            handler.complete();

            data.incrementLevel(player);
        }
    }

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void handleOnMove(PlayerMoveEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        boolean isNearArchery = event.getNewPosition().distance(ARCHERY_POSITION) < 0.5;
        boolean wasNearArchery = event.getPlayer().getPosition().distance(ARCHERY_POSITION) < 0.5;

        if (isNearArchery == wasNearArchery) return;

        if (isNearArchery) {
            Map<Integer, SkyBlockItem> bows = player.getAllOfComponentInInventory(BowComponent.class);
            if (bows.isEmpty()) {
                player.sendMessage("<c>You need a bow to shoot the targets! Purchase one from the Weaponsmith upstairs!");
                return;
            }

            DatapointArcheryPractice.ArcheryPracticeData data = player.getArcheryPracticeData();
            DatapointArcheryPractice.TargetPracticeLevels practiceLevel = data.getTargetPracticeLevel();

            if (practiceLevel == DatapointArcheryPractice.TargetPracticeLevels.CONCLUDED) {
                player.sendMessage("<c>You have already completed all Target Practice levels!");
                return;
            }

            startArcheryPractice(player, practiceLevel);
            return;
        }

        if (TimedActivityHandler.hasActiveHandler(player)) {
            TimedActivityHandler.getActiveHandler(player).cancel();
        }
    }

    private static void startArcheryPractice(SkyBlockPlayer player, DatapointArcheryPractice.TargetPracticeLevels practiceLevel) {
        TimedActivityHandler handler = TimedActivityHandler.<SkyBlockPlayer>builder(player)
                .countdownSeconds(3)
                .activityDurationSeconds(practiceLevel.getTimeLimitSeconds())
                .onCountdownTick((p, remaining) -> {
                    Text countdown = switch (remaining) {
                        case 3 -> Text.of("<e>{}", remaining);
                        case 2 -> Text.of("<6>{}", remaining);
                        case 1 -> Text.of("<c>{}", remaining);
                        default -> Text.of("<f>{}", remaining);
                    };

                    p.showTitle(
                            countdown,
                            Text.of("<c>Shoot all the lanterns!"),
                            Title.Times.times(Duration.ZERO, Duration.ofMillis(1100), Duration.ZERO)
                    );
                })
                .onActivityStart(p -> {
                    p.showTitle(
                            Text.of("<a>GO!"),
                            Text.empty(),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                    );

                    updateActionBar(p, practiceLevel.getTimeLimitSeconds(), 0);
                })
                .onActivityTick((p, elapsed) -> {
                    int remaining = practiceLevel.getTimeLimitSeconds() - elapsed;
                    int targetsHit = p.getArcheryPracticeData().getTargetsHitList().size();
                    updateActionBar(p, remaining, targetsHit);
                })
                .onActivityComplete(p -> {
                    int targetsHit = p.getArcheryPracticeData().getTargetsHitList().size();
                    if (targetsHit >= TOTAL_TARGETS) {
                        p.sendMessage("<a>You've successfully completed Target Practice {:roman}", practiceLevel.getLevelNumber());
                    } else {
                        p.sendMessage("<c>You ran out of time!");
                    }
                    p.getArcheryPracticeData().resetTargetsHit();
                })
                .onActivityCancelled(p -> {
                    p.sendMessage("<c>Cancelled! You cannot leave the pressure plate!");
                    p.getArcheryPracticeData().resetTargetsHit();
                })
                .build();
        handler.start();
    }

    private static void updateActionBar(SkyBlockPlayer player, int timeRemaining, int targetsHit) {
        SkyBlockActionBar actionBar = SkyBlockActionBar.getFor(player);

        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.HEALTH,
                Text.of("<e>Time Left: <f>{}s", timeRemaining),
                25,
                100
        );

        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.DEFENSE,
                Text.empty(),
                25,
                100
        );

        actionBar.addReplacement(
                SkyBlockActionBar.BarSection.MANA,
                Text.of("<e>Targets: <a>{} <e>/ <a>{}", targetsHit, TOTAL_TARGETS),
                25,
                100
        );
    }
}
