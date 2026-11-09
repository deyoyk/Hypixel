package com.deyo.hypixel.type.skyblockgeneric.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.SharedInstance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class SkyBlockRegenConfiguration {

    public final Map<Pos, MiningTask> activeMiningTasks = new LinkedHashMap<>();

    public abstract MiningTask handleStageOne(MiningTask task, Pos brokenBlock);

    public abstract MiningTask handleStageTwo(MiningTask task, Pos brokenBlock);

    public abstract List<Material> getMineableBlocks(Instance instance, Point point);

    public abstract long getRegenerationTime();

    public void addToQueue(SkyBlockPlayer player, Pos block, SharedInstance instance) {
        Optional<MiningTask> taskExists = findMiningTask(block);

        if (taskExists.isPresent()) {
            // Stage 2 Handler
            var task = handleStageTwo(new MiningTask(
                    player,
                    block,
                    null,
                    null,
                    instance,
                    System.currentTimeMillis() + getRegenerationTime(),
                    2,
                    taskExists.get().getInitialReviveBlock(),
                    taskExists.get().getInitialMinedBlock()), block);
            if (task.getIntermediaryBlock() == null || task.getReviveBlock() == null) {
                // There is no stage 2 for this MiningConfiguration
                return;
            }

            instance.setBlock(block, task.getIntermediaryBlock());
            putTask(task);
        } else {
            // Stage 1 Handler
            var task = handleStageOne(new MiningTask(
                    player,
                    block,
                    null,
                    null,
                    instance,
                    System.currentTimeMillis() + getRegenerationTime(),
                    1,
                    null,
                    instance.getBlock(block)), block);
            task.setInitialReviveBlock(task.getReviveBlock());
            putTask(task);
            instance.setBlock(block, task.getIntermediaryBlock());
        }
    }

    void putTask(MiningTask task) {
        activeMiningTasks.put(task.getPosition(), task);
    }

    Optional<MiningTask> findMiningTask(Pos block) {
        return Optional.ofNullable(activeMiningTasks.get(block));
    }

    public Optional<Block> getInitialBlockFromPos(Pos block) {
        return findMiningTask(block).map(MiningTask::getInitialReviveBlock);
    }

    public void removeExistingTask(Pos block) {
        activeMiningTasks.remove(block);
    }

    public static void startRepeater(Scheduler scheduler) {
        scheduler.submitTask(() -> {
            long now = System.currentTimeMillis();
            for (RegionType regionType : RegionType.values()) {
                SkyBlockRegenConfiguration mining = regionType.getMiningHandler();
                if (mining == null) continue;

                mining.tickTasks(now);
            }
            return TaskSchedule.tick(20);
        });
    }

    void tickTasks(long now) {
        Iterator<Map.Entry<Pos, MiningTask>> iterator = activeMiningTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            MiningTask task = iterator.next().getValue();
            if (task.getRegenerationTime() > now) continue;

            task.getInstance().setBlock(task.getPosition(), task.getReviveBlock());
            if (task.getStage() == 2) {
                reschedule(task, now, getRegenerationTime());
            } else {
                iterator.remove();
            }
        }
    }

    static void reschedule(MiningTask task, long now, long regenerationTime) {
        task.setStage(1);
        task.setRegenerationTime(now + regenerationTime);
        task.setIntermediaryBlock(task.getReviveBlock());
        task.setReviveBlock(task.getInitialReviveBlock());
    }

    public static Block getRandomBlock(RegenerationConfig... regenerationConfigs) {
        int totalChance = Arrays.stream(regenerationConfigs).mapToInt(RegenerationConfig::chance).sum();
        if (totalChance <= 0) return null;
        return getRandomBlock(ThreadLocalRandom.current().nextInt(totalChance), regenerationConfigs);
    }

    static Block getRandomBlock(int roll, RegenerationConfig... regenerationConfigs) {
        int currentCumulativeChance = 0;

        for (RegenerationConfig config : regenerationConfigs) {
            currentCumulativeChance += config.chance();
            if (roll < currentCumulativeChance) {
                return config.block();
            }
        }
        return null;
    }

    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MiningTask {
        SkyBlockPlayer playerWhoInitiated;
        Pos position;
        Block intermediaryBlock;
        Block reviveBlock;
        SharedInstance instance;
        long regenerationTime;
        int stage;
        Block initialReviveBlock;
        Block initialMinedBlock;

        public static MiningTask never() {
            return MiningTask.builder().build();
        }
    }

    public record RegenerationConfig(int chance, Block block) { }
}