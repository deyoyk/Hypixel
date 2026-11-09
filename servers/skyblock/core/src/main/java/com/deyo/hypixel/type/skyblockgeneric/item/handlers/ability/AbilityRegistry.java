package com.deyo.hypixel.type.skyblockgeneric.item.handlers.ability;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.CustomBlockBreakEvent;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.ability.abilities.BuildersWandAbility;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.statistics.TemporaryStatistic;
import com.deyo.hypixel.type.skyblockgeneric.utility.TeleportSafety;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AbilityRegistry {
    private static final Map<String, RegisteredAbility> REGISTERED_ABILITIES = new HashMap<>();

    static {
        register(new RegisteredAbility(
                "DRAGON_RAGE",
                "Dragon Rage",
                (_, _) -> "<7>All Monsters in front of you take <a>12,000 <7>damage. Hit monsters take large knockback.",
                RegisteredAbility.AbilityActivation.RIGHT_CLICK,
                0,
                new RegisteredAbility.AbilityManaCost(100),
                (player, _, _, _) -> {
                    for (Entity entity : player.getInstance().getNearbyEntities(player.getPosition(), 6)) {
                        if (!(entity instanceof SkyBlockMob mob)) continue;
                        mob.damage(new Damage(DamageType.MAGIC, player, player, player.getPosition(), 12000));
                        mob.setVelocity(mob.getPosition().sub(player.getPosition()).asVec().normalize().mul(14).withY(7));
                    }
                    return true;
                }
        ));

        register(new RegisteredAbility(
                "PARLEY",
                "Parley",
                (_, _) -> "<7>Channel your inner Jerry.",
                RegisteredAbility.AbilityActivation.RIGHT_CLICK,
                20,
                new RegisteredAbility.AbilityManaCost(10),
                (player, _, _, _) -> {
                    player.sendMessage("<a>Jerry!");
                    return true;
                }
        ));

        register(new RegisteredAbility(
            "WITHER_IMPACT",
            "Wither Impact",
            (player, item) -> "<7>Teleports <a>10 Blocks <7>ahead of you. Then implode dealing <c>10000 <7>damage to nearby enemies. Also applies the wither shield scroll ability reducing mobdamage taken and granting an absorption shield for <e>5 <7>seconds.",
            RegisteredAbility.AbilityActivation.RIGHT_CLICK,
            50,
            new RegisteredAbility.AbilityManaCost(25),
            (player, _, _, _) -> {
                BlockVec tpPos = player.getPosition().add(player.getPosition().direction().mul(10)).asBlockVec();
                Point destination = tpPos.add(0, 1, 0);
                if (!player.getInstance().getBlock(destination).air() || !player.getInstance().getBlock(destination.add(0, 1, 0)).air())
                    return false;
                if (!TeleportSafety.hasClearPath(player.getInstance(), player.getPosition(), destination))
                    return false;
                player.teleport(destination.asPos(player.getPosition().yaw(), player.getPosition().pitch()));
                for (Entity entity : player.getInstance().getNearbyEntities(player.getPosition(), 6)) {
                    if (entity instanceof SkyBlockMob mob) {
                        mob.damage(new Damage(DamageType.MAGIC, player, player, player.getPosition(), 10000));
                    }
                }
                return true;
            }
        ));

        register(new RegisteredAbility(
            "INSTANT_TRANSMISSION",
            "Instant Transmission",
            (player, item) -> "<7>Teleports <a>8 Blocks <7>ahead of you and gain <a>+50 <f>Speed for <a>3 seconds<7>.",
            RegisteredAbility.AbilityActivation.RIGHT_CLICK,
            5,
            new RegisteredAbility.AbilityManaCost(50),
            (player, _, _, _) -> {
                BlockVec tpPos = player.getPosition().add(player.getPosition().direction().mul(8)).asBlockVec();
                Point destination = tpPos.add(0, 1, 0);
                if (!player.getInstance().getBlock(destination).air() || !player.getInstance().getBlock(destination.add(0, 1, 0)).air())
                    return false;
                if (!TeleportSafety.hasClearPath(player.getInstance(), player.getPosition(), destination))
                    return false;
                player.teleport(new Pos(destination, player.getPosition().yaw(), player.getPosition().pitch()));
                ItemStatistics speedStats = ItemStatistics.builder().withBase(ItemStatistic.SPEED, 50.0).build();
                TemporaryStatistic speedBoost = TemporaryStatistic.builder().withStatistics(speedStats).withExpirationInMs(3000).withDisplayName("Instant Transmission").build();
                player.getStatistics().boostStatistic(speedBoost);
                return true;
            }
        ));

        register(new RegisteredAbility(
            "ETHER_TRANSMISSION",
            "Ether Transmission",
            (_, _) -> "<7>Teleport to your targeted block up to <a>57 <7>blocks away.",
            RegisteredAbility.AbilityActivation.SNEAK_RIGHT_CLICK,
            5,
            new RegisteredAbility.AbilityManaSoulflowCost(180, 1),
            (player, _, _, _) -> {
                Point targetedBlock = player.getTargetBlockPosition(57);
                if (targetedBlock == null) return false;
                BlockVec tpPos = targetedBlock.asBlockVec();
                Point destination = tpPos.add(0, 1, 0);
                if (!player.getInstance().getBlock(destination).air() || !player.getInstance().getBlock(destination.add(0, 1, 0)).air())
                    return false;
                if (!TeleportSafety.hasClearPath(player.getInstance(), player.getPosition(), destination))
                    return false;
                player.teleport(destination.asPos(player.getPosition().yaw(), player.getPosition().pitch()));
                return true;
            }
        ));

        register(new RegisteredAbility(
            "TRUE_DWARTH",
            "True Dwarth",
            (_, _) -> "<7>Shows the way towards the nearest <6>Emissary <7>while in the <2>Dwarven Mines<7>.",
            RegisteredAbility.AbilityActivation.RIGHT_CLICK,
            20 * 3,
            new RegisteredAbility.NoAbilityCost(),
            (player, _, _, _) -> {
                if (player.getRegion() == null || player.getRegion().getType() != RegionType.DWARVEN_MINES) {
                    player.sendMessage("<c>You must be in the Dwarven Mines to use this ability!");
                    return false;
                }

                List<Pos> emissaries = HypixelNPC.getRegisteredNPCs().stream()
                        .filter(npc -> npc.getName(player).startsWith("Emissary"))
                        .map(npc -> npc.getParameters().position(player))
                        .filter(Objects::nonNull)
                        .toList();
                Pos nearest = nearestPosition(player.getPosition(), emissaries);
                if (nearest == null) {
                    player.sendMessage("<c>No Emissaries found.");
                    return false;
                }

                player.sendMessage("<6>Nearest Emissary: <e>{}, {}, {}",
                        nearest.blockX(), nearest.blockY(), nearest.blockZ());
                return true;
            }
        ));

        register(new RegisteredAbility(
            "SPEED_BOOST",
            "Speed Boost",
            (_, _) -> "<7>Grants <7>+100 Speed",
            RegisteredAbility.AbilityActivation.RIGHT_CLICK,
            20 * 5,
            new RegisteredAbility.AbilityManaCost(50),
            (player, _, _, _) -> {
                ItemStatistics speedStats = ItemStatistics.builder().withBase(ItemStatistic.SPEED, 100.0).build();
                TemporaryStatistic speedBoost = TemporaryStatistic.builder().withStatistics(speedStats).withExpirationInMs(30000).withDisplayName("Speed Boost").build();
                player.getStatistics().boostStatistic(speedBoost);
                return true;
            }
        ));

        register(new RegisteredPassiveAbility(
            "STORED_POTENTIAL",
            "Stored Potential",
            (_, item) -> {
                int blocksMined = item.getAttributeHandler().getStoredPotential();
                int miningSpeed = Math.min((blocksMined / 100) * 10, 250);
                return """
                    <7>Grants <6>+10⸕ Mining Speed <7>for every
                    100 blocks mined.
                    <8>(Max +250⸕ Mining Speed)

                    <7>Blocks Mined: <a>%d
                    <7>Current Bonus: <a>%d""".formatted(blocksMined, miningSpeed);
            },
            List.of(new RegisteredPassiveAbility.Action<>(
                CustomBlockBreakEvent.class,
                EventNodes.CUSTOM,
                event -> {
                    SkyBlockPlayer player = event.getPlayer();
                    player.updateItemInSlot(player.getHeldSlot(), (item) -> {
                        item.getAttributeHandler().setStoredPotential(item.getAttributeHandler().getStoredPotential() + 1);
                        item.getAttributeHandler().setExtraDynamicStatistics(
                            ItemStatistics.builder().withAdditive(
                                ItemStatistic.MINING_SPEED, (double) Math.min((item.getAttributeHandler().getStoredPotential() / 100) * 10, 250)
                            ).build()
                        );
                    });
                },
                RegisteredPassiveAbility.Action.createDefaultCondition("STORED_POTENTIAL")
            ))
        ));

//		register(new RegisteredPassiveAbility(
//				"BEJEWELED_BLADE",
//				"Bejeweled Blade",
//				"<7>Deals <a>+150% <7>damage to mobs on <b>Mining Islands.",
//				List.of(new RegisteredPassiveAbility.Action<>(
//
//				))
//		));

        register(new BuildersWandAbility());
    }

    static Pos nearestPosition(Pos origin, List<Pos> positions) {
        return positions.stream()
                .min(Comparator.comparingDouble(origin::distance))
                .orElse(null);
    }

    public static void register(RegisteredAbility ability) {
        REGISTERED_ABILITIES.put(ability.getId(), ability);
    }

    public static RegisteredAbility getAbility(String id) {
        return REGISTERED_ABILITIES.get(id);
    }

    public static Map<String, RegisteredAbility> getRegisteredAbilities() {
        return REGISTERED_ABILITIES;
    }

}
