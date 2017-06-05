package com.javatao.heartbeat;

// 回调
public interface IHBCallback {
    void add(IPorts ports);

    void remove(IPorts ports);
}
