package com.deyo.hypixel.commons.redis;

import org.json.JSONObject;

public record RedisEnvelope(String id, String from, String payload) {
    public String serialize() {
        StringBuilder json = new StringBuilder(payload != null ? payload.length() + 64 : 64);
        json.append("{\"id\":").append(JSONObject.quote(id))
                .append(",\"from\":").append(JSONObject.quote(from))
                .append(",\"payload\":").append(rawPayload(payload))
                .append('}');
        return json.toString();
    }

    private static String rawPayload(String payload) {
        if (payload == null || payload.isEmpty()) {
            return "\"\"";
        }
        char first = payload.charAt(0);
        if (first == '{' || first == '[') {
            return payload;
        }
        return JSONObject.quote(payload);
    }

    public static RedisEnvelope deserialize(String json) {
        JSONObject obj = new JSONObject(json);
        return new RedisEnvelope(
                obj.getString("id"),
                obj.getString("from"),
                obj.optString("payload", "")
        );
    }
}
