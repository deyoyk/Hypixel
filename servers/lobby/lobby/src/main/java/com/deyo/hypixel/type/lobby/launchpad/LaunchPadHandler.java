package com.deyo.hypixel.type.lobby.launchpad;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LaunchPadHandler {
    private static final List<LaunchPad> launchPads = new ArrayList<>();

    public static void register(Scheduler scheduler, List<LaunchPad> pads) {
        launchPads.clear();
        launchPads.addAll(pads);

        if (launchPads.isEmpty()) return;

        scheduler.scheduleTask(() -> {
            for (LaunchPad launchPad : launchPads) {
                for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                    HypixelGenericLoader.getLoadedPlayers().forEach(player -> {
                        if (slimeBlock.distance(player.getPosition()) <= 15) {
                            player.sendPacket(new ParticlePacket(
                                    Particle.COMPOSTER,
                                    false,
                                    false,
                                    slimeBlock.x() + 0.5,
                                    slimeBlock.y() + 1,
                                    slimeBlock.z() + 0.5,
                                    0.1f,
                                    0.1f,
                                    0.1f,
                                    0f,
                                    3
                            ));
                        }
                    });
                }
            }
        }, TaskSchedule.seconds(2), TaskSchedule.tick(5), ExecutionType.TICK_END);
    }

    public static @Nullable LaunchPad getLaunchPadInRange(Pos pos, double range) {
        if (launchPads.isEmpty()) return null;

        for (LaunchPad launchPad : launchPads) {
            for (Pos slimeBlock : launchPad.getSlimeBlocks()) {
                if (slimeBlock.distance(pos) <= range) {
                    return launchPad;
                }
            }
        }
        return null;
    }

    public static List<Pos> getSlimeBlocksNear(Pos pos) {
        Instance instance = HypixelConst.getInstanceContainer();
        if (instance == null) return new ArrayList<>();
        List<Pos> toReturn = new ArrayList<>();

        int minChunkX = (pos.blockX() - 5) >> 4;
        int maxChunkX = (pos.blockX() + 5) >> 4;
        int minChunkZ = (pos.blockZ() - 5) >> 4;
        int maxChunkZ = (pos.blockZ() + 5) >> 4;
        List<java.util.concurrent.CompletableFuture<net.minestom.server.instance.Chunk>> loads = new ArrayList<>();
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                if (instance.getChunk(chunkX, chunkZ) == null) {
                    loads.add(instance.loadChunk(chunkX, chunkZ));
                }
            }
        }
        if (!loads.isEmpty()) {
            java.util.concurrent.CompletableFuture.allOf(
                    loads.toArray(java.util.concurrent.CompletableFuture[]::new)).join();
        }

        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                for (int y = -5; y <= 5; y++) {
                    Pos check = new Pos(pos.x() + x, pos.y() + y, pos.z() + z);
                    Block block = instance.getBlock(check);

                    if (block == Block.SLIME_BLOCK)
                        toReturn.add(check);
                    if (block == Block.BARRIER)
                        instance.setBlock(check, Block.AIR);
                }
            }
        }

        return toReturn;
    }
}
