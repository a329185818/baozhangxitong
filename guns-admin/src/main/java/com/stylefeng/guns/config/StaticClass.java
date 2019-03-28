package com.stylefeng.guns.config;

import java.util.UUID;

public class StaticClass {
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr = str.replace("-", "");
        return uuidStr;
    }
}
