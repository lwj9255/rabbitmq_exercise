package com.hhu.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GlobalCache {
    private static Map map = new HashMap();

    public static void set(String key, Object value){
        map.put(key, value);
    }

    public static Object get(String key){
        return map.get(key);
    }

    public static void remove(String key){
        map.remove(key);
    }
}
