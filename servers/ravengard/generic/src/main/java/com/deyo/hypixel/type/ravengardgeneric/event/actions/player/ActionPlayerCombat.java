package com.deyo.hypixel.type.ravengardgeneric.event.actions.player;

import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.item.ItemStack;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.ravengardgeneric.entity.mob.RavengardMob;
import com.deyo.hypixel.type.ravengardgeneric.item.RavengardItemType;
import com.deyo.hypixel.type.ravengardgeneric.item.attribute.RavengardItemAttributeHandler;
import com.deyo.hypixel.type.ravengardgeneric.item.statistics.RavengardItemStatistic;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;

public class ActionPlayerCombat implements HypixelEventClass {
    private static final double DISPLAY_DAMAGE_SCALE = 4.0;
    private static final float FIST_DAMAGE = 1f;

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void onAttack(EntityAttackEvent event) {
        if (!(event.getEntity() instanceof RavengardPlayer player)
                || !(event.getTarget() instanceof RavengardMob mob)) {
            return;
        }

        ItemStack held = player.getItemInMainHand();
        RavengardItemAttributeHandler handler = new RavengardItemAttributeHandler(held);
        RavengardItemType type = handler.getType();
        if (type != null && !type.usableBy(player.getRavengardClass())) {
            player.sendMessage("<c>Your class cannot use this item!");
            return;
        }

        float damage = FIST_DAMAGE;
        if (type != null) {
            double weapon = type.statistic(RavengardItemStatistic.DAMAGE) * handler.getStatBoost();
            if (weapon > 0) {
                damage = (float) Math.max(FIST_DAMAGE, weapon / DISPLAY_DAMAGE_SCALE);
            }
        }
        mob.damage(DamageType.PLAYER_ATTACK, damage);
    }
}
