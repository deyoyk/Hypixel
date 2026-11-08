package com.deyo.hypixel.type.bedwarsgame.shop;

import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.bedwars.BedWarsGameType;

import java.util.function.Function;

public record UpgradeableItemTier(String name, Function<BedWarsGameType, Integer> price, Currency currency, Material material) {
}

