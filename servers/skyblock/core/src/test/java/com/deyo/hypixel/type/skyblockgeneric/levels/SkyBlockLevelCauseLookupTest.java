package com.deyo.hypixel.type.skyblockgeneric.levels;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.LevelCause;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.MuseumLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.NewAccessoryLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.levels.causes.SkillLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumRewards;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class SkyBlockLevelCauseLookupTest {

    private Map<String, SkyBlockLevelCauseAbstr> causes;
    private Map<String, SkyBlockLevelCauseAbstr> backup;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void installCauses() throws Exception {
        Field field = SkyBlockLevelCause.class.getDeclaredField("CAUSES");
        field.setAccessible(true);
        causes = (Map<String, SkyBlockLevelCauseAbstr>) field.get(null);

        backup = new HashMap<>(causes);
        causes.clear();
        causes.put("level-10", new LevelCause(10));
        causes.put("skill-MINING-1", new SkillLevelCause(SkillCategories.MINING, 1));
        causes.put("accessory-FARMING_TALISMAN", new NewAccessoryLevelCause(ItemType.FARMING_TALISMAN));
        causes.put("museum-REWARD_1", new MuseumLevelCause(MuseumRewards.REWARD_1));
    }

    @AfterEach
    void restoreCauses() {
        causes.clear();
        causes.putAll(backup);
    }

    @Test
    void directLookupsResolveRegisteredCauses() {
        LevelCause level = SkyBlockLevelCause.getLevelCause(10);

        assertSame(causes.get("level-10"), level);
        assertSame(causes.get("skill-MINING-1"),
                SkyBlockLevelCause.getSkillCause(SkillCategories.MINING, 1));
        assertSame(causes.get("accessory-FARMING_TALISMAN"),
                SkyBlockLevelCause.getAccessoryCause(ItemType.FARMING_TALISMAN));
        assertSame(causes.get("museum-REWARD_1"),
                SkyBlockLevelCause.getMuseumCause(MuseumRewards.REWARD_1));
    }

    @Test
    void missingCausesResolveToNull() {
        assertNull(SkyBlockLevelCause.getLevelCause(999));
        assertNull(SkyBlockLevelCause.getSkillCause(SkillCategories.MINING, 99));
        assertNull(SkyBlockLevelCause.getAccessoryCause(ItemType.ZOMBIE_TALISMAN));
    }
}
