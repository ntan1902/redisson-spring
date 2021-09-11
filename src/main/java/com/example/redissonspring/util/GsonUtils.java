package com.example.redissonspring.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Base64;

public final class GsonUtils {

  private static final Gson GSON =
      new GsonBuilder()
          .registerTypeAdapter(
              byte[].class,
              new TypeAdapter<byte[]>() {
                @Override
                public void write(JsonWriter out, byte[] value) throws IOException {
                  out.value(Base64.getEncoder().encodeToString(value));
                }

                @Override
                public byte[] read(JsonReader in) throws IOException {
                  return Base64.getDecoder().decode(in.nextString());
                }
              })
          .create();

  public static <T> T fromJson(String json, Class<T> clazz) {
    return GSON.fromJson(json, clazz);
  }

  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }
}
