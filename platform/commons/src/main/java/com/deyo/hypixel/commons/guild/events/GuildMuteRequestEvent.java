package com.deyo.hypixel.commons.guild.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildMuteRequestEvent extends GuildEvent {
    @NotNull
    private UUID muter;
    @Nullable
    private UUID target;
    private long duration;

    public GuildMuteRequestEvent(@NotNull UUID muter, @Nullable UUID target, long duration) {
        super(null);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(muter);
    }

}
