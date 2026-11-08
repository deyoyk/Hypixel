package com.deyo.hypixel.type.skyblockgeneric.fishing.resolver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.deyo.hypixel.type.skyblockgeneric.fishing.FishingContext;
import com.deyo.hypixel.type.skyblockgeneric.fishing.catches.CatchPayload;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FishingCatchResolver {

    public static CatchPayload resolve(FishingContext context) {
        return FishingLootResolver.resolve(context);
    }
}
