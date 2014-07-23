package com.wdeanmedical.ehr.util;

import com.google.gson.Gson;

public final class JSONUtils {
  private static final Gson gson = new Gson();

  private JSONUtils() {
  }

  public static boolean isJSONValid(String JSON_STRING, Class clazz) {
    try {
      gson.fromJson(JSON_STRING, clazz);
      return true;
    } catch (com.google.gson.JsonSyntaxException jse) {
      return false;
    } catch (com.google.gson.JsonParseException jpe) {
      return false;
    }
  }
}