package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import net.minestom.server.entity.PlayerHand;
import com.deyo.hypixel.anticheat.math.Pos;

import java.util.UUID;

@Getter
public class UseEntityPacket extends DeyoPacket {
    private final int entityId;
    private final Pos targetPosition;
    private final PlayerHand hand;

    public UseEntityPacket(UUID uuid, int entityId, Pos targetPosition, PlayerHand hand) {
        super(uuid);
        this.entityId = entityId;
        this.targetPosition = targetPosition;
        this.hand = hand;
    }
}
