package com.deyo.hypixel.type.bedwarsgame.item.impl;

import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.utility.CooldownTracker;

import java.util.UUID;

public final class LuckyEquipmentEffects {
    private static final CooldownTracker CHICKEN_HAT_COOLDOWN = new CooldownTracker();
    private static final CooldownTracker SQUID_BOOTS_COOLDOWN = new CooldownTracker();

    private LuckyEquipmentEffects() {
    }

    public static void tick(BedWarsPlayer player) {
        handleHelmet(player, player.getHelmet());
        handleBoots(player, player.getBoots());
    }

    public static ItemStack equipment(String name, Material material, String action, String... lore) {
        return new LuckySpecialItem().item(name, material, action, 0, lore);
    }

    private static void handleHelmet(BedWarsPlayer player, ItemStack helmet) {
        String action = LuckyCombatEffects.action(helmet);
        if (action == null) return;

        switch (action) {
            case "frog_helmet" -> {
                player.addEffect(new Potion(PotionEffect.JUMP_BOOST, (byte) 4, 40));
                player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 40));
            }
            case "hot_head" -> {
                player.addEffect(new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 60));
                player.addEffect(new Potion(PotionEffect.SPEED, (byte) 0, 40));
            }
            case "chicken_hat" -> chickenHat(player);
            case "exodus" -> player.addEffect(new Potion(PotionEffect.REGENERATION, (byte) 0, 40));
            default -> {
            }
        }
    }

    private static void handleBoots(BedWarsPlayer player, ItemStack boots) {
        String action = LuckyCombatEffects.action(boots);
        if (action == null) return;

        switch (action) {
            case "slime_boots" -> {
                player.addEffect(new Potion(PotionEffect.SLOW_FALLING, (byte) 0, 40));
                if (player.isOnGround() && player.getVelocity().y() < -0.1) {
                    player.setVelocity(player.getVelocity().withY(7));
                }
            }
            case "squid_boots" -> squidBoots(player);
            case "heat_boots" -> player.addEffect(new Potion(PotionEffect.FIRE_RESISTANCE, (byte) 0, 60));
            default -> {
            }
        }
    }

    public static void clear(UUID playerId) {
        CHICKEN_HAT_COOLDOWN.clear(playerId);
        SQUID_BOOTS_COOLDOWN.clear(playerId);
    }

    private static void chickenHat(BedWarsPlayer player) {
        player.getGame().getPlayers().stream()
            .filter(other -> other != player && !other.getTeamKey().equals(player.getTeamKey()))
            .filter(other -> other.getPosition().distance(player.getPosition()) <= 16)
            .findFirst()
            .ifPresent(target -> {
                if (!CHICKEN_HAT_COOLDOWN.tryUse(player.getUuid(), 2500L)) {
                    return;
                }
                target.setVelocity(target.getVelocity().add(player.getPosition().direction().mul(5)).add(0, 2, 0));
            });
    }

    private static void squidBoots(BedWarsPlayer player) {
        var point = player.getPosition().sub(0, 1, 0);
        if (!player.getInstance().getBlock(point).air()) {
            return;
        }
        if (!SQUID_BOOTS_COOLDOWN.tryUse(player.getUuid(), 750L)) {
            return;
        }
        player.getInstance().setBlock(point, Block.WATER.withTag(TypeBedWarsGameLoader.PLAYER_PLACED_TAG, true));
    }
}
