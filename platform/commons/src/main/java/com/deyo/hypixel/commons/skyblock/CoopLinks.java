package com.deyo.hypixel.commons.skyblock;

import com.deyo.hypixel.libs.LinkType;
import com.deyo.hypixel.libs.PlayerField;
import com.deyo.hypixel.libs.codec.Codecs;

import java.util.UUID;

public final class CoopLinks {
    public static final PlayerField<UUID> COOP_KEY = PlayerField.create("skyblock", "_coop_key", Codecs.UUID, null);
    public static final LinkType<UUID> COOP = LinkType.create("coop", Codecs.UUID, COOP_KEY);

    private CoopLinks() {}
}
