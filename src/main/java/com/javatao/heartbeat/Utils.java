package com.javatao.heartbeat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    // json to ArrayMap
    public static List<Map<String, Object>> toArrayMap(String str) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (str != null) {
            if (str.length() < 3) {
                return list;
            }
            String sb = str.trim().substring(1, str.length() - 2);
            String[] split = sb.split("[}]\\W*[{]");
            for (String ss : split) {
                ss = ss.trim();
                if (!ss.startsWith("{")) {
                    ss = "{" + ss;
                }
                if (!ss.endsWith("}")) {
                    ss = ss + "}";
                }
                Map<String, Object> out = toMap(ss);
                list.add(out);
            }
        }
        return list;
    }

    ////
    public static Map<String, Object> toMap(String str) {
        String sb = str.substring(1, str.length() - 1);
        String[] name = sb.split(",");
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < name.length; i++) {
            String ss = name[i];
            String[] nn = ss.split(":");
            if (nn.length < 2) {
                continue;
            }
            String key = nn[0];
            String value = nn[1];
            key = key.trim();
            value = value.trim();
            if (key.startsWith("\"")) {
                key = key.substring(1);
            }
            if (key.endsWith("\"")) {
                key = key.substring(0, key.length() - 1);
            }
            if (value.startsWith("\"")) {
                value = value.substring(1);
            }
            if (value.endsWith("\"")) {
                value = value.substring(0, value.length() - 1);
            }
            map.put(key, value);
        }
        return map;
    }
}
