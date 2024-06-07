package org.example;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultMapper {
    private static ObjectMapper instance;

    private DefaultMapper() {}

    public static ObjectMapper getInstance() {
        if (instance == null) {
            // Use synchronized block to ensure thread safety
            synchronized (DefaultMapper.class) {
                if (instance == null) {
                    instance = new ObjectMapper();
                    instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                }
            }
        }
        return instance;
    }
}
