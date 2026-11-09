package com.deyo.hypixel.commons.redis;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedisEnvelopeTest {

    @Test
    void roundTripsNestedJsonPayloadsWithoutEscapingThePayload() {
        String payload = "{\"name\":\"a\\\"b\",\"nested\":{\"values\":[1,2,3]}}";
        RedisEnvelope envelope = new RedisEnvelope("id-1", "server-1", payload);

        String serialized = envelope.serialize();
        RedisEnvelope decoded = RedisEnvelope.deserialize(serialized);

        assertEquals("id-1", decoded.id());
        assertEquals("server-1", decoded.from());
        assertTrue(new JSONObject(payload).similar(new JSONObject(decoded.payload())));
    }

    @Test
    void roundTripsPlainStringPayloads() {
        RedisEnvelope envelope = new RedisEnvelope("id", "from", "plain text");
        assertEquals("plain text", RedisEnvelope.deserialize(envelope.serialize()).payload());
    }
}
