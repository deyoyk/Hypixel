package com.deyo.hypixel.type.skyblockgeneric.warps.unlocks;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.warps.ScrollUnlockReason;

public class ScrollUnlockCustomRecipe extends ScrollUnlockReason {
    private final String subReason;

    public ScrollUnlockCustomRecipe(String subReason) {
        this.subReason = subReason;
    }

    @Override
    public Text getTitleReason() {
        return Text.of("<c>Scroll crafted from recipe!");
    }

    @Override
    public Text getSubReason() {
        return Text.of("<7>{}", subReason);
    }
}
