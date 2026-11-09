package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;
import com.deyo.hypixel.anticheat.math.Pos;
import com.deyo.hypixel.anticheat.math.Vel;

public class JesusFlag extends Flag {

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Player player = onlinePlayer(event.getPlayer());
        if (player == null || player.getInstance() == null) return;

        Pos currentPos = event.getCurrentTick().getPos();
        Vel currentVel = event.getCurrentTick().getVel();
        boolean onGround = event.getCurrentTick().isOnGround();

        if (onGround) {
            checkWaterWalking(event, player, currentPos, currentVel);
        }

        checkFloatingOnWater(event, player, currentPos, currentVel);
    }

    private void checkWaterWalking(PlayerPositionUpdateEvent event, Player player, Pos pos, Vel vel) {
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y() - 0.1);
        int blockZ = (int) Math.floor(pos.z());

        Block block = player.getInstance().getBlock(blockX, blockY, blockZ);
        if (isLiquid(block)) {
            double horizontalSpeed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

            if (horizontalSpeed > 0.05) {
                double certainty = Math.min(0.95, 0.7 + horizontalSpeed * 2);
                event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.JESUS, certainty);
            } else {
                event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.JESUS, 0.6);
            }
        }
    }

    private void checkFloatingOnWater(PlayerPositionUpdateEvent event, Player player, Pos pos, Vel vel) {
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y());
        int blockZ = (int) Math.floor(pos.z());

        Block block = player.getInstance().getBlock(blockX, blockY, blockZ);
        if (isLiquid(block)) {
            if (event.getPreviousTick() != null) {
                Pos prevPos = event.getPreviousTick().getPos();
                double yChange = pos.y() - prevPos.y();

                if (Math.abs(yChange) < 0.01) {
                    event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.JESUS, 0.75);
                } else if (yChange > 0 && Math.abs(vel.y()) < 0.05) {
                    event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.JESUS, 0.8);
                }
            }

            double horizontalSpeed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

            if (horizontalSpeed > 0.15) {
                double certainty = Math.min(0.9, 0.6 + (horizontalSpeed - 0.15) * 3);
                event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.JESUS, certainty);
            }
        }
    }

    static boolean isLiquid(Block block) {
        return block != null && block.liquid();
    }
}
