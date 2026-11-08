package com.deyo.hypixel.type.skywarsgame.events;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skywars.SkywarsGameType;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skywarsgame.TypeSkywarsGameLoader;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGame;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGameStatus;
import com.deyo.hypixel.type.skywarsgame.luckyblock.LuckyBlock;
import com.deyo.hypixel.type.skywarsgame.luckyblock.LuckyBlockType;
import com.deyo.hypixel.type.skywarsgame.user.SkywarsPlayer;

public class ActionLuckyBlockPlace implements HypixelEventClass {
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void onBlockPlace(PlayerBlockPlaceEvent event) {
        if (!(event.getPlayer() instanceof SkywarsPlayer player)) return;

        ItemStack itemInHand = player.getItemInMainHand();
        if (itemInHand.material() != Material.PLAYER_HEAD) return;

        String luckyBlockTypeName = itemInHand.getTag(LuckyBlockType.LUCKY_BLOCK_TYPE_TAG);
        if (luckyBlockTypeName == null) return;

        LuckyBlockType type = LuckyBlockType.fromName(luckyBlockTypeName);
        if (type == null) return;

        event.setCancelled(true);

        SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
        if (game == null) return;

        if (game.getGameType() != SkywarsGameType.SOLO_LUCKY_BLOCK) return;

        if (game.getGameStatus() != SkywarsGameStatus.IN_PROGRESS) return;

        LuckyBlock luckyBlockManager = game.getLuckyBlockManager();
        if (luckyBlockManager == null) return;

        Pos blockPos = new Pos(event.getBlockPosition());
        luckyBlockManager.placeLuckyBlock(blockPos, type);

        ItemStack newStack = itemInHand.withAmount(itemInHand.amount() - 1);
        player.setItemInMainHand(newStack);
    }
}
