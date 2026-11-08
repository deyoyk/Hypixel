package com.deyo.hypixel.service.api.endpoints;

import com.deyo.hypixel.commons.protocol.objects.api.APIAuthenticateCodeProtocol;
import com.deyo.hypixel.service.api.APIAdminDatabase;
import com.deyo.hypixel.service.api.APIAdminDatabaseObject;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.jetbrains.annotations.Nullable;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointAuthenticateCode implements RedisMessageHandler<
        APIAuthenticateCodeProtocol.AuthenticateCodeMessage,
        APIAuthenticateCodeProtocol.AuthenticateCodeResponse> {

    @Override
    public APIAuthenticateCodeProtocol protocol() {
        return new APIAuthenticateCodeProtocol();
    }

    @Override
    public APIAuthenticateCodeProtocol.AuthenticateCodeResponse handle(APIAuthenticateCodeProtocol.AuthenticateCodeMessage messageObject, RedisMessageContext context) {
        @Nullable APIAdminDatabaseObject document = APIAdminDatabase.getFromCode(messageObject.authCode());

        if (document == null) {
            return new APIAuthenticateCodeProtocol.AuthenticateCodeResponse(false, "Authentication failed");
        }

        document.setAuthenticatorName(messageObject.playerName());
        document.setAuthenticatorUUID(messageObject.playerUUID());

        APIAdminDatabase.replaceOrInsert(document);

        return new APIAuthenticateCodeProtocol.AuthenticateCodeResponse(true, null);
    }
}
