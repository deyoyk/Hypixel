package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.event.AntiCheatListener;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.AnticheatPacketEvent;
import com.deyo.hypixel.anticheat.event.packet.PingResponsePacket;
import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;

public class PingEvents extends AntiCheatListener {
    @ListenerMethod
    public void onPacketReceive(AnticheatPacketEvent event) {
        DeyoPacket packet = event.packet();
        if (packet instanceof PingResponsePacket pingResponsePacket) {
            DeyoPlayer player = pingResponsePacket.getPlayer();
            if (player != null) player.handlePingResponse(pingResponsePacket.getRequestId());
        }
    }
}
