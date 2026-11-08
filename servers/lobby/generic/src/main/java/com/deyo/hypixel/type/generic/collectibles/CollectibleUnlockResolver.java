package com.deyo.hypixel.type.generic.collectibles;

import com.deyo.hypixel.type.generic.user.HypixelPlayer;

@FunctionalInterface
public interface CollectibleUnlockResolver {
    boolean isUnlocked(HypixelPlayer player, CollectibleDefinition definition);
}
