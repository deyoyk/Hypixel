package com.deyo.hypixel.type.skyblockgeneric.levels.unlocks;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.gui.inventory.ItemStacks;
import com.deyo.hypixel.type.skyblockgeneric.levels.CustomLevelAward;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelUnlock;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

@Getter
public class CustomLevelUnlock extends SkyBlockLevelUnlock {
    private final CustomLevelAward award;
    public CustomLevelUnlock(CustomLevelAward award) {
        this.award = award;
    }

    @Override
    public UnlockType type() {
        return UnlockType.CUSTOM;
    }

    @Override
    public ItemStack.Builder getItemDisplay(SkyBlockPlayer player, int level) {
        return ItemStacks.item(Material.GOLDEN_APPLE, 1, award.getDisplay(),
                List.of(Text.of("<8>Level {}", level)));
    }

    @Override
    public List<Text> getDisplay(SkyBlockPlayer player, int level) {
        return List.of(award.getDisplay());
    }
}
