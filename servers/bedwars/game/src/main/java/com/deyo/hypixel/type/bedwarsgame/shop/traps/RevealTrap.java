package com.deyo.hypixel.type.bedwarsgame.shop.traps;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.shop.Currency;
import com.deyo.hypixel.type.bedwarsgame.shop.Trap;
import com.deyo.hypixel.type.bedwarsgame.shop.TrapId;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;

public class RevealTrap extends Trap {

	public RevealTrap() {
		super(
			TrapId.REVEAL,
				"Reveal Trap",
				ItemStack.of(Material.REDSTONE_TORCH),
				"Reveals intruders by glowing for 10s.",
				Currency.DIAMOND
		);
	}

	@Override
	public void onTrigger(BedWarsGame game, BedWarsMapsConfig.TeamKey teamName, BedWarsPlayer triggerer) {
		triggerer.setGlowing(true);
		ScheduleUtility.delay(
				() -> triggerer.setGlowing(false),
				10 * 20
		);
	}
}
