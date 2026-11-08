package com.deyo.hypixel.type.skyblockgeneric.item.handlers.customstatistics;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;

import java.util.function.Function;

public record CustomStatisticsConfig(Function<SkyBlockItem, ItemStatistics> statisticsProvider) {
}
