package org.example;

import com.google.gson.Gson;

public class DefaultGson {
    private static Gson instance;

    private DefaultGson() {}

    public static Gson getInstance() {
        if (instance == null) {
            // Use synchronized block to ensure thread safety
            synchronized (DefaultGson.class) {
                if (instance == null) {
                    instance = new Gson();
                }
            }
        }
        return instance;
    }
}
