package com.javatao.heartbeat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MainRun {
    public static void main(String[] args) throws IOException {
        // 读取节点
        URL uri = HBConfig.class.getResource("/nodes.json");
        InputStream in = uri.openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = in.read()) != -1) {
            baos.write(i);
        }
        String s = baos.toString();
        // 转换map
        List<Map<String, Object>> list = Utils.toArrayMap(s);
        // 通用配置参数
        Properties pro = Utils.loadConfig();
        String glbCallback = pro.getProperty("callback");
        // 包装对象
        List<IPorts> iPorts = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String host = (String) map.get("host");
            Integer port = Integer.valueOf((String) map.get("port"));
            String callback = (String) map.get("callback");
            IPorts ports = new IPorts(host, port);
            if (callback == null) {
                callback = glbCallback;
            }
            if (!"false".equalsIgnoreCase(callback)) {
                ports.setCallback(callback);
            }
            iPorts.add(ports);
        }
        new HBConfig().initNode(iPorts);
    }
}
