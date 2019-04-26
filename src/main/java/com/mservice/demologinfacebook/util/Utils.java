package com.mservice.demologinfacebook.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class Utils {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T fromString(String jSon, Class<T> type) throws IOException {
        return mapper.readValue(jSon, type);
    }

    public static <T> T fromString(byte[] json, Class<T> type) throws IOException {
        return mapper.readValue(json, type);
    }

    public static <T> T fromString(File jSon, Class<T> type) throws IOException {
        return mapper.readValue(jSon, type);
    }

    public static String toString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public static JsonNode getJsonFromString(String json) throws IOException {
        return mapper.readTree(json);
    }

    public static String nullToEmpty(Object object) {
        return object == null ? "" : object.toString();
    }

    public static String doCreateUrlWithParams(String url, Map<String, Object> params) {
        return url + "?" + Utils.buildStringParams(params);
    }

    public static String buildStringParams(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();

        params.forEach((key, value) -> {
            builder.append(key)
                    .append("=")
                    .append(value)
                    .append("&");
        });

        String val = builder.toString();
        // cat chuoi

        if (val.length() > 1) {
            val = val.substring(0, val.length() - 1);
        }

        return val;

    }

    public static String encode64(String s) {
        // encode data on your side using BASE64
        byte[] bytesEncoded = Base64.getEncoder().encode(s.getBytes());
        return new String(bytesEncoded);
    }

    public static boolean isEmpty(Object object) {
        return object == null ? true : (object instanceof String && ((String) object).trim().isEmpty());
    }

    public static String encodeBase64URL(byte[] s) {
        if (Utils.isEmpty(s)) {
            return "";
        }
        return new String(Base64.getUrlEncoder().encode(s));
    }

    public static byte[] decodeBase64URL(byte[] s) {
        return Base64.getUrlDecoder().decode(s);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
            closeable = null;
        }
        catch (Exception ex) {
            // NOOP
        }
    }
}
