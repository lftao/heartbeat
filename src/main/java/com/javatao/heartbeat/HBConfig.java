package com.javatao.heartbeat;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 心跳检测
 * 
 * @author tao
 */
public class HBConfig {
    // 所有的集合
    private Set<IPorts> sockets = new HashSet<>();
    // 健康的集合
    private List<IPorts> healthy = new AList<IPorts>();
    // 定时调度
    ScheduledExecutorService scheduled = Executors.newSingleThreadScheduledExecutor();

    // 构造方法
    public HBConfig() {
        super();
        // 每秒检测
        scheduled.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                for (IPorts ports : sockets) {
                    ports.check(healthy);
                }
            }
        }, 3000L, 1000L, TimeUnit.MILLISECONDS);
    }

    // 初始化节点
    public void initNode(List<IPorts> iPorts) {
        for (IPorts ports : iPorts) {
            addNode(ports);
        }
    }

    // 添加节点
    public void addNode(IPorts iPorts) {
        sockets.add(iPorts);
        iPorts.check(healthy);
    }

    // 随机取值
    private Random random = new Random();

    // 获得监控的节点
    public String getHealthy() {
        // 随机取值
        String host = get().getHost();
        return host;
    }

    private IPorts get() {
        int size = healthy.size();
        if (size == 0) {
            throw new IllegalArgumentException("not healthy ip : port");
        }
        // 随机取值
        IPorts ports = healthy.get(random.nextInt(size));
        boolean status = ports.getStatus();
        if (status) {
            return ports;
        } else {
            healthy.remove(ports);
            return get();
        }
    }
}
