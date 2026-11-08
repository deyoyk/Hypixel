package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.mobdamage;

import net.minestom.server.event.entity.projectile.ProjectileCollideWithEntityEvent;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.ArrowEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeEffectService;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.statistics.PlayerStatistics;
import com.deyo.hypixel.type.skyblockgeneric.utility.AttackService;

import java.util.Map;

public class PlayerActionArrowDamageMob implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(ProjectileCollideWithEntityEvent event) {
        ArrowEntityImpl arrow;
        if (event.getEntity() instanceof ArrowEntityImpl arrowEntity)
            arrow = arrowEntity;
        else return;

        SkyBlockMob collidedWith;
        if (event.getTarget() instanceof SkyBlockMob mob)
            collidedWith = mob;
        else return;

        SkyBlockPlayer shooter;
        if (arrow.getShooter() instanceof SkyBlockPlayer player)
            shooter = player;
        else return;

        SkyBlockItem arrowItem = arrow.getArrowItem();
        ItemStatistics entityStats = mob.getStatistics();
        ItemStatistics playerStats = shooter.getStatistics().allStatistics();

        // Add the arrow's statistics to the player's statistics
        playerStats = ItemStatistics.add(playerStats, arrowItem.getAttributeHandler().getStatistics());

        Map.Entry<Double, Boolean> hit = PlayerStatistics.runPrimaryDamageFormula(playerStats, entityStats);
        double damage = hit.getKey() * AttributeEffectService.outgoingDamageMultiplier(shooter.getHuntingData(), collidedWith, true);
        boolean critical = hit.getValue();

        float damageAmount = (float) damage;
        if (!AttackService.applyHit(shooter, collidedWith, damageAmount, critical)) return;

        double ferocity = playerStats.getOverall(ItemStatistic.FEROCITY);
        AttackService.scheduleExtraHits(shooter, collidedWith, damageAmount, critical, ferocity);
    }
}
