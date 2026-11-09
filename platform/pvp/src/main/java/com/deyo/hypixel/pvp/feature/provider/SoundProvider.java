package com.deyo.hypixel.pvp.feature.provider;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import com.deyo.hypixel.pvp.feature.CombatFeature;
import org.jetbrains.annotations.NotNull;

public interface SoundProvider extends CombatFeature {
    SoundProvider DEFAULT = (audience, sound, x, y, z) -> audience.playSound(sound, x, y, z);

    void playSound(Audience audience, final @NotNull Sound original, final double x, final double y, final double z);
}
