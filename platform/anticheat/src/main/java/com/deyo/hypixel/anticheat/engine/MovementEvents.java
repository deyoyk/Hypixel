package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.event.AntiCheatListener;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.AnticheatPacketEvent;
import com.deyo.hypixel.anticheat.event.packet.IsOnGroundPacket;
import com.deyo.hypixel.anticheat.event.packet.PositionAndRotationPacket;
import com.deyo.hypixel.anticheat.event.packet.PositionPacket;
import com.deyo.hypixel.anticheat.event.packet.RotationPacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.math.Pos;

public class MovementEvents extends AntiCheatListener {
    @ListenerMethod
    public void onPacketReceive(AnticheatPacketEvent event) {
        DeyoPacket packet = event.packet();
        DeyoPlayer player = packet.getPlayer();

        Pos pos = player.getCurrentTick().getPos();
        boolean onGround = player.getCurrentTick().isOnGround();

        switch (packet) {
            case PositionPacket positionPacket -> {
                pos = new Pos(positionPacket.getX(),
                        positionPacket.getY(), positionPacket.getZ(),
                        pos.yaw(), pos.pitch());
                onGround = positionPacket.isOnGround();
            }
            case PositionAndRotationPacket positionAndRotationPacket -> {
                pos = positionAndRotationPacket.getPos();
                onGround = positionAndRotationPacket.isOnGround();
            }
            case IsOnGroundPacket isOnGroundPacket -> onGround = isOnGroundPacket.isOnGround();
            case RotationPacket rotationPacket ->
                    pos = new Pos(pos.x(), pos.y(), pos.z(), rotationPacket.getYaw(), rotationPacket.getPitch());
            default -> {
            }
        }

        player.processMovement(pos, onGround);
    }
}
