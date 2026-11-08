package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.api.APIAuthenticateCodeProtocol;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "allowaccessforapiauthentication verifyapi",
        description = "Allows access for API authentication for the given code",
        usage = "/allowaccessforapiauthentication <code>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class VerifyApiCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString code = new ArgumentString("code");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("<7>Checking if API is online...");

            Thread.startVirtualThread(() -> {
                if (!(new ProxyService(ServiceType.API).isOnline().join())) {
                    sender.sendMessage("<c>API is offline.");
                    return;
                }

                sender.sendMessage("<7>API is online, checking if code is valid...");
                String codeString = context.get(code);

                SkyBlockPlayer player = (SkyBlockPlayer) sender;
                ProxyService apiService = new ProxyService(ServiceType.API);
                apiService.handleRequest(new APIAuthenticateCodeProtocol.AuthenticateCodeMessage(
                        codeString,
                        player.getUsername(),
                        player.getUuid()
                )).thenAccept(nonCastedResponse -> {
                    APIAuthenticateCodeProtocol.AuthenticateCodeResponse response = (APIAuthenticateCodeProtocol.AuthenticateCodeResponse) nonCastedResponse;
                    if (response.success()) {
                        sender.sendMessage(Text.of("<a>Code '{}' has successfully been verified! Check your web browser.", codeString));
                    } else {
                        sender.sendMessage("<c>Invalid code!");
                    }
                });
            });
        }, code);
    }
}
