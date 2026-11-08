package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import com.deyo.hypixel.commons.Songs;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.noteblock.SkyBlockSong;
import com.deyo.hypixel.type.skyblockgeneric.noteblock.SkyBlockSongsHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "playsong playmusic",
        description = "Plays a song",
        usage = "/playmusic <song>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PlayMusicCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<Songs> song = new ArgumentEnum<>("song", Songs.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            if (!SkyBlockSongsHandler.isEnabled) {
                player.sendMessage("<c>SkyBlock songs are not enabled on this server.");
                return;
            }

            player.sendMessage("<a>Loading song <e>{}<a>...", context.get(song).name());
            SkyBlockSong skyBlockSong;
            try {
                skyBlockSong = new SkyBlockSong(context.get(song));
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("<c>Error loading song: {}", e.getMessage());
                return;
            }
            player.sendMessage("<a>Loaded song <e>{}<a>, now playing...", context.get(song).name());
            SkyBlockSongsHandler songsHandler = new SkyBlockSongsHandler(player);
            player.sendMessage("<a>Playing song <e>{}<a>...", skyBlockSong.getSong().name());
            songsHandler.setPlayerSong(skyBlockSong);

            player.sendMessage("<a>Playing song <e>{}<a>.", skyBlockSong.getSong().name());
        }, song);
    }
}
