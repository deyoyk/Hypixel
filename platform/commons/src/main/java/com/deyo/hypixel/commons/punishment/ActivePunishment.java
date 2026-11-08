package com.deyo.hypixel.commons.punishment;

import java.util.List;

public record ActivePunishment(String type, String banId, PunishmentReason reason, long expiresAt, List<PunishmentTag> tags) {}
