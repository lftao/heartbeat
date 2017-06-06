package com.javatao.heartbeat;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 工具
 * 
 * @author tao
 */
public class Utils {
    private static Properties pro;

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

    public static Properties loadConfig() {
        if (pro == null) {
            pro = new Properties();
        } else {
            return pro;
        }
        try {
            URL cfp = HBConfig.class.getResource("/config.properties");
            pro.load(cfp.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pro;
    }

    public static void sendEmail(String content,String receiveds) {
        try {
            Properties props = Utils.loadConfig();
            String swh = props.getProperty("mail.switch");
            if (!"on".equalsIgnoreCase(swh)) {
                return;
            }
            // 收件人
            List<InternetAddress> addrs = new ArrayList<>();
            if (receiveds == null) {
                receiveds = props.getProperty("mail.received");
            }
            for (String ved : receiveds.split(",")) {
                if (ved != null && ved.trim().length() > 0) {
                    addrs.add(new InternetAddress(ved));
                }
            }
            if (addrs.size() <= 0) {
                return;
            }
            // 设置环境信息
            Session session = Session.getInstance(props);
            // 创建邮件对象
            Message msg = new MimeMessage(session);
            String subject = props.getProperty("mail.subject");
            if (subject == null) {
                subject = "heartbeat Monitor";
            }
            msg.setSubject(subject);
            // 设置邮件内容
            // msg.setText(contentMsg);
            // MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();// 创建一个包含附件内容的MimeBodyPart
            // 设置HTML内容
            messageBodyPart.setContent(content, "text/html; charset=utf-8");
            mainPart.addBodyPart(messageBodyPart);
            msg.setContent(mainPart);
            // 设置发件人
            String user = props.getProperty("mail.user");
            String password = props.getProperty("mail.password");
            msg.setFrom(new InternetAddress(user));
            Transport transport = session.getTransport();
            // 连接邮件服务器
            transport.connect(user, password);
            transport.sendMessage(msg, addrs.toArray(new InternetAddress[] {}));
            // 关闭连接
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
