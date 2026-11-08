package com.deyo.hypixel.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.velocity.SkyBlockVelocity;
import com.deyo.hypixel.velocity.gamemanager.TransferHandler;

public class LimboCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player player)) {
            return;
        }
        player.getCurrentServer().ifPresent((connection) -> {
            if (connection.getServer() == SkyBlockVelocity.getLimboServer()) {
                for (int i = 0; i < 5; i++) {
                    player.sendMessage(Text.empty());
                }
                player.sendMessage(Text.of("<c>The lobby you attempted to join was full or offline."));
                player.sendMessage(Text.of("<e>Because of this, you were routed to Limbo, a subset of your own imagination."));
                player.sendMessage(Text.of("<d>This place doesn't exist anywhere, any you can stay here as long as you'd like."));
                player.sendMessage(Text.of("<6>To return to \"reality\", use <b>/lobby GAME."));
                player.sendMessage(Text.of("<c>Examples: /lobby, /lobby skywars, /lobby arcade"));
                player.sendMessage(Text.of("<4>Watch out, though, as there are things that live in Limbo."));
                return;
            }

            TransferHandler transferHandler = new TransferHandler(player);
            transferHandler.sendToLimbo();
        });
    }
}
