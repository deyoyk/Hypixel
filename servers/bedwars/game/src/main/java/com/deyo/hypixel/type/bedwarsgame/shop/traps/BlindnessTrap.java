package com.deyo.hypixel.type.bedwarsgame.shop.traps;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.Trap;
import com.deyo.hypixel.type.bedwarsgame.shop.TrapId;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;

public class BlindnessTrap extends Trap {

	public BlindnessTrap() {
		super(
			TrapId.BLINDNESS,
				"Blindness Trap",
				ItemStack.of(Material.TRIPWIRE_HOOK),
				"Blinds intruders for 10 seconds.",
				Currency.DIAMOND
		);
	}

	@Override
	public void onTrigger(BedWarsGame game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer triggerer) {
		triggerer.addEffect(new Potion(PotionEffect.BLINDNESS, (byte) 1, 200));
	}
}
