package com.deyo.hypixel.type.replayviewer.playback;

import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.game.replay.recordable.Recordable;
import org.tinylog.Logger;

import java.util.Collections;
import java.util.List;

public class ReplaySeeker {

    public static void seekForward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(currentTick + 1, targetTick);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.newStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        int recordables = applyPersistentRecordables(session, currentTick + 1, targetTick);

        Logger.debug("Seeked forward from {} to {} ({} recordables)", currentTick, targetTick, recordables);
    }

    public static void seekBackward(ReplaySession session, int currentTick, int targetTick) {
        ReplayData data = session.getReplayData();

        List<ReplayData.BlockChangeEntry> blockChanges = data.getBlockChangesBetween(targetTick + 1, currentTick);
        Collections.reverse(blockChanges);
        for (var bc : blockChanges) {
            Block block = Block.fromStateId(bc.previousStateId());
            if (block != null) {
                session.getInstance().setBlock(bc.x(), bc.y(), bc.z(), block);
            }
        }

        rebuildStateAtTick(session, targetTick);

        Logger.debug("Seeked backward from {} to {}", currentTick, targetTick);
    }

    public static void rebuildStateAtTick(ReplaySession session, int targetTick) {
        ReplayData data = session.getReplayData();

        session.beginStateRebuild();
        try {
            session.getEntityManager().cleanup();
            session.getDroppedItemManager().clear();
            session.getDynamicTextManager().cleanup();
            session.getNpcManager().cleanup();
            session.getStateTracker().clear();

            int recordables = applyPersistentRecordables(session, 0, targetTick);

            Logger.debug("Rebuilt replay state at tick {} ({} recordables)", targetTick, recordables);
        } finally {
            session.endStateRebuild();
        }
    }

    private static int applyPersistentRecordables(ReplaySession session, int startTick, int endTick) {
        int[] count = {0};
        session.getReplayData().forEachRecordableBetween(startTick, endTick, recordable -> {
            if (!isPersistentRecordable(recordable)) {
                return;
            }

            count[0]++;
            try {
                RecordablePlayer.play(recordable, session);
            } catch (Exception e) {
                Logger.error(e, "Failed to apply seek recordable {} at tick {}", recordable.getType(), recordable.getTick());
            }
        });
        return count[0];
    }

    static boolean isPersistentRecordable(Recordable recordable) {
        return switch (recordable.getType()) {
            case ENTITY_SPAWN,
                 ENTITY_DESPAWN,
                 ITEM_PICKUP,
                 PLAYER_DEATH,
                 PLAYER_RESPAWN,
                 ENTITY_EFFECT -> true;
            default -> recordable.isEntityState();
        };
    }
}
