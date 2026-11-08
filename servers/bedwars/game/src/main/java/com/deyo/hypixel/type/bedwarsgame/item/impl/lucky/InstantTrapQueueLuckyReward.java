package com.deyo.hypixel.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.type.bedwarsgame.shop.TrapId;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;

import java.util.List;

public class InstantTrapQueueLuckyReward extends LuckyReward {
    public InstantTrapQueueLuckyReward() {
        super("Instant Trap Queue");
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        List<TrapId> traps = List.of(TrapId.BLINDNESS, TrapId.COUNTER_OFFENSIVE, TrapId.REVEAL);
        traps.forEach(trap -> player.getGame().addTeamTrap(player.getTeamKey(), trap));
        player.sendMessage("<a>Filled your trap queue.");
    }
}
