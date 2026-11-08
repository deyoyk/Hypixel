package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.mobdamage;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.EntityDamage;
import net.minestom.server.event.entity.EntityAttackEvent;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.mobs.slayer.SlayerBossMob;
import com.deyo.hypixel.type.skyblockgeneric.event.value.SkyBlockValueEvent;
import com.deyo.hypixel.type.skyblockgeneric.event.value.events.PlayerDamagedByMobValueUpdateEvent;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeEffectService;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.statistics.PlayerStatistics;
import com.deyo.hypixel.type.skyblockgeneric.utility.DamageIndicator;

import java.util.Map;

public class PlayerActionDamagedAttacked implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(EntityAttackEvent event) {
        if (!event.getTarget().getEntityType().equals(EntityType.PLAYER)) return;

        SkyBlockPlayer player = (SkyBlockPlayer) event.getTarget();

        if (event.getEntity() instanceof SkyBlockMob mob) {
            if (mob.getLastAttack() + mob.damageCooldown() > System.currentTimeMillis()) return;
            mob.setLastAttack(System.currentTimeMillis());

            ItemStatistics mobStatistics = mob.getStatistics();
            ItemStatistics playerStatistics = player.getStatistics().allStatistics();

            Map.Entry<Double, Boolean> damageDealt =
                    PlayerStatistics.runPrimaryDamageFormula(mobStatistics, playerStatistics);

            double baseDefense = playerStatistics.getOverall(ItemStatistic.DEFENSE);
            double resistance = AttributeEffectService.resistanceDefense(player.getHuntingData(), mob);
            double resistanceMultiplier = (100D + Math.max(0, baseDefense))
                    / (100D + Math.max(0, baseDefense + resistance));
            PlayerDamagedByMobValueUpdateEvent valueEvent = new PlayerDamagedByMobValueUpdateEvent(
                    player, (float) (damageDealt.getKey() * resistanceMultiplier), mob);
            SkyBlockValueEvent.callValueUpdateEvent(valueEvent);

            // Handle damage event pets — they may further modify the damage taken
            float finalDamage = (float) valueEvent.getValue();
            SkyBlockItem pet = player.getPetData().getEnabledPet();
            PetEvent.DamagedByMob damageEvent = player.getPetData()
                    .dispatch(new PetEvent.DamagedByMob(player, pet, mob, finalDamage));
            finalDamage = (float) damageEvent.damage();

            player.damage(new EntityDamage(mob, finalDamage));

            if (mob instanceof SlayerBossMob slayerBoss) {
                slayerBoss.getAbility().onMeleeHit(slayerBoss, player);
            }

            new DamageIndicator()
                    .damage(finalDamage)
                    .pos(player.getPosition())
                    .critical(damageDealt.getValue())
                    .display(player.getInstance());
        }
    }

}
