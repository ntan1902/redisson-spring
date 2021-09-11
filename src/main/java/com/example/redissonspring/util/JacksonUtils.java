package com.example.redissonspring.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JacksonUtils {
    private static final ObjectMapper OM = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return OM.readValue(json, clazz);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        return OM.writeValueAsString(obj);
    }
}
