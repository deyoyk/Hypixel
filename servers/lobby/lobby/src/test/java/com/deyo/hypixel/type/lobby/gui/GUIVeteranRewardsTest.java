package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.type.generic.experience.LevelReward;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIVeteranRewardsTest {

    @Test
    void filtersAndOrdersVeteranRewards() {
        List<LevelReward> rewards = List.of(
                reward(150),
                reward(99),
                reward(100),
                reward(125)
        );

        assertEquals(List.of(100, 125, 150), GUIVeteranRewards.veteranRewards(rewards).stream()
                .map(LevelReward::getLevel)
                .toList());
    }

    private static LevelReward reward(int level) {
        return LevelReward.builder().level(level).coins(1).build();
    }
}
