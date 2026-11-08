package com.deyo.hypixel.type.bedwarsgame.shop.upgrades;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.TeamUpgrade;
import com.deyo.hypixel.type.bedwarsgame.shop.TeamUpgradeId;
import com.deyo.hypixel.type.bedwarsgame.shop.TeamUpgradeTier;

import java.util.List;

public class ManiacMinerUpgrade extends TeamUpgrade {

	public ManiacMinerUpgrade() {
		super(
			TeamUpgradeId.MANIAC_MINER,
				"Maniac Miner",
				"All players on your team permanently gain Haste.",
				ItemStack.of(Material.GOLDEN_PICKAXE),
				List.of(
						new TeamUpgradeTier(1, "Haste I", 2, Currency.DIAMOND),
						new TeamUpgradeTier(2, "Haste II", 4, Currency.DIAMOND)
				)
		);
	}

	public static void giveHaste(Player player, int level) {
		player.addEffect(new Potion(PotionEffect.HASTE, (byte) (level - 1), Potion.INFINITE_DURATION));
		player.getAttribute(Attribute.MINING_EFFICIENCY).addModifier(new AttributeModifier("bw:maniac_miner", 0.2 * level, AttributeOperation.ADD_MULTIPLIED_TOTAL));
	}

	@Override
	public void applyEffect(BedWarsGame game, BedWarsMapsConfig.TeamKey teamName, int level) {
		game.getPlayers().stream()
				.filter(p -> teamName.equals(p.getTeamKey()))
				.forEach(player -> giveHaste(player, level));
	}
}
