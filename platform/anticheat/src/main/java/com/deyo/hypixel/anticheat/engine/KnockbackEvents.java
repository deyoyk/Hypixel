package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.event.AntiCheatListener;
import com.deyo.hypixel.anticheat.event.DeyoEventHandler;
import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.AnticheatPacketEvent;
import com.deyo.hypixel.anticheat.event.events.PlayerKnockbackEvent;
import com.deyo.hypixel.anticheat.event.packet.KnockbackPacket;

public class KnockbackEvents extends AntiCheatListener {

    @ListenerMethod
    public void onPacketReceive(AnticheatPacketEvent event) {
        PlayerKnockbackEvent knockback = toEvent(event);
        if (knockback != null) {
            DeyoEventHandler.callEvent(knockback);
        }
    }

    static PlayerKnockbackEvent toEvent(AnticheatPacketEvent event) {
        if (!(event.packet() instanceof KnockbackPacket packet) || packet.getPlayer() == null) {
            return null;
        }
        return new PlayerKnockbackEvent(packet.getPlayer(), packet.getVelocity());
    }
}
