package com.deyo.hypixel.type.bedwarsgame.item.impl.lucky;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;

public class BlockLuckyReward extends LuckyReward {
    private final Block block;

    public BlockLuckyReward(String name, Block block) {
        super(name);
        this.block = block;
    }

    @Override
    public void apply(BedWarsPlayer player, Pos openedAt) {
        player.getInstance().setBlock(openedAt, block.withTag(TypeBedWarsGameLoader.PLAYER_PLACED_TAG, true));
    }
}
