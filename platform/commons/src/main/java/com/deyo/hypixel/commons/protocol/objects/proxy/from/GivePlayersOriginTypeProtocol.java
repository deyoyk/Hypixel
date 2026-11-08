package com.deyo.hypixel.commons.protocol.objects.proxy.from;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class GivePlayersOriginTypeProtocol extends RedisProtocol<
        GivePlayersOriginTypeProtocol.Request,
        GivePlayersOriginTypeProtocol.Response> {
    private static final Serializer<Request> SERIALIZER =
            new JacksonSerializer<>(Request.class);
    private static final Serializer<Response> RETURN_SERIALIZER =
            new JacksonSerializer<>(Response.class);

    @Override
    public Serializer<Request> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<Response> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record Request(String uuid, String originType) {}
    public record Response() {}
}
