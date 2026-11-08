package com.deyo.hypixel.anticheat.loader;

import com.deyo.hypixel.anticheat.flag.FlagType;

import java.util.UUID;

public abstract class PunishmentHandler {
    public abstract void onFlag(UUID uuid, FlagType flagType);
}
