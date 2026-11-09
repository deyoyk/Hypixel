package com.deyo.hypixel.anticheat.flag.flags;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;
import com.deyo.hypixel.anticheat.math.Pos;
import com.deyo.hypixel.anticheat.math.Vel;

public class PhaseFlag extends Flag {

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        Player player = onlinePlayer(event.getPlayer());
        if (player == null || player.getInstance() == null) return;

        Pos currentPos = event.getCurrentTick().getPos();
        checkCollision(event, player.getInstance(), currentPos);
    }

    private void checkCollision(PlayerPositionUpdateEvent event, Instance instance, Pos pos) {
        int blockX = (int) Math.floor(pos.x());
        int blockY = (int) Math.floor(pos.y());
        int blockZ = (int) Math.floor(pos.z());

        Block feet = instance.getBlock(blockX, blockY, blockZ);
        Block head = instance.getBlock(blockX, blockY + 1, blockZ);

        boolean feetSolid = isSolid(feet);
        boolean headSolid = isSolid(head);

        if (feetSolid || headSolid) {
            Vel vel = event.getCurrentTick().getVel();
            double speed = Math.sqrt(vel.x() * vel.x() + vel.z() * vel.z());

            if (speed > 0.05) {
                double certainty = Math.min(0.95, 0.7 + speed * 0.5);
                event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.PHASE, certainty);
            } else {
                event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.PHASE, 0.5);
            }
        }

        if (event.getPreviousTick() != null) {
            Pos prevPos = event.getPreviousTick().getPos();
            checkWallClipping(event, instance, prevPos, pos);
        }
    }

    private void checkWallClipping(PlayerPositionUpdateEvent event, Instance instance, Pos from, Pos to) {
        double dx = to.x() - from.x();
        double dy = to.y() - from.y();
        double dz = to.z() - from.z();

        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance < 0.1) return;

        double midX = (from.x() + to.x()) / 2;
        double midY = (from.y() + to.y()) / 2;
        double midZ = (from.z() + to.z()) / 2;

        Block block = instance.getBlock(
            (int) Math.floor(midX),
            (int) Math.floor(midY),
            (int) Math.floor(midZ)
        );
        if (isSolid(block)) {
            event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.PHASE, 0.8);
        }
    }

    static boolean isSolid(Block block) {
        return block != null && block.solid();
    }
}
