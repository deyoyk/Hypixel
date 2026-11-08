package com.deyo.hypixel.pvp.feature.provider;

import net.minestom.server.Viewable;
import net.minestom.server.entity.Entity;
import net.minestom.server.network.packet.server.ServerPacket;
import com.deyo.hypixel.pvp.feature.CombatFeature;

public interface PacketProvider extends CombatFeature {
    PacketProvider DEFAULT = Viewable::sendPacketToViewersAndSelf;

    void sendPacket(Entity viewable, ServerPacket.Play packet);
}
