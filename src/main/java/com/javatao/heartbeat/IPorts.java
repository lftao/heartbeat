package com.javatao.heartbeat;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

// host ip
public class IPorts {
    private String host;
    private Integer port;
    private boolean status;
    private Socket socket;
    private IHBCallback callback;

    public IPorts() {
        super();
    }

    public IPorts(String host, Integer port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public int hashCode() {
        return (host + port).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPorts) {
            IPorts po = (IPorts) obj;
            String hts = po.getHost();
            Integer prt = po.getPort();
            if (hts != null && prt != null) {
                return hts.equals(host) && prt.equals(port);
            }
        }
        return super.equals(obj);
    }

    public IPorts check(List<IPorts> healthy) {
        try {
            if (socket != null && !socket.isClosed() && socket.getKeepAlive()) {
                if (!status) {
                    HBLoggers.IPortsLogger.info("Heartbeat InetAddress add " + this);
                    setStatus(true);
                    healthy.add(this);
                }
            } else {
                // 移除
                if (status) {
                    HBLoggers.IPortsLogger.info("Heartbeat InetAddress remove " + this);
                    setStatus(false);
                    healthy.remove(this);
                } else {
                    initSocket();
                }
            }
        } catch (Exception e) {
            HBLoggers.IPortsLogger.severe(this + ",error:" + e.getMessage());
        }
        return this;
    }

    private void initSocket() throws IOException {
        if (socket != null) {
            socket.close();
        }
        socket = new Socket(host, port);
        socket.setKeepAlive(true);
    }

    @Override
    public String toString() {
        return "host:" + host + ",port:" + port;
    }

    public IHBCallback getCallback() {
        return this.callback;
    }

    public void setCallback(IHBCallback callback) {
        this.callback = callback;
    }

    public void setCallback(String callback) {
        try {
            Object instance = Class.forName(callback).newInstance();
            this.callback = (IHBCallback) instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
