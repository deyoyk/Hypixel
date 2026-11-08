package com.deyo.hypixel.type.skyblockgeneric.warps.unlocks;

import com.deyo.hypixel.commons.StringUtility;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.warps.ScrollUnlockReason;

public class ScrollUnlockPurchase extends ScrollUnlockReason {
    private final String npc;

    public ScrollUnlockPurchase(String npc) {
        this.npc = npc;
    }

    @Override
    public Text getTitleReason() {
        return Text.of("<c>Scroll bought from NPC!");
    }

    @Override
    public Text getSubReason() {
        return Text.of("<7>Purchase the scroll from the {}.", StringUtility.toNormalCase(npc));
    }
}
