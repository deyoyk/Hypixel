package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.darkauction.TriggerDarkAuctionProtocol;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.calendar.SkyBlockCalendar;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "forcedarkauction", description = "Force starts a Dark Auction",
        usage = "/forcedarkauction",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ForceDarkAuctionCommand extends HypixelCommand {
    private static final ProxyService darkAuctionService = new ProxyService(ServiceType.DARK_AUCTION);

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.sendMessage("<7>Triggering Dark Auction...");

            if (!darkAuctionService.isOnline().join()) {
                player.sendMessage("<c>Dark Auction service is currently offline.");
                return;
            }

            TriggerDarkAuctionProtocol.TriggerMessage message =
                    new TriggerDarkAuctionProtocol.TriggerMessage(SkyBlockCalendar.getElapsed(), true);

            darkAuctionService.handleRequest(message)
                    .thenAccept(response -> {
                        if (response instanceof TriggerDarkAuctionProtocol.TriggerResponse triggerResponse) {
                            if (triggerResponse.success()) {
                                player.sendMessage("<a>Dark Auction started successfully!");
                            } else {
                                player.sendMessage("<c>Failed to start Dark Auction: {}", triggerResponse.message());
                            }
                        }
                    })
                    .exceptionally(throwable -> {
                        player.sendMessage("<c>Error triggering Dark Auction: {}", throwable.getMessage());
                        return null;
                    });
        });
    }
}
