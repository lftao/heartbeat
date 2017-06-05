package com.javatao.heartbeat;

import java.util.ArrayList;

public class AList<T> extends ArrayList<T> {
    /**
     * 
     */
    private static final long serialVersionUID = -3972711668327364392L;

    @Override
    public boolean add(T e) {
        try {
            if (e instanceof IPorts) {
                IPorts ports = (IPorts) e;
                IHBCallback callback = ports.getCallback();
                if (callback != null) {
                    callback.add(ports);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return super.add(e);
    }

    @Override
    public boolean remove(Object o) {
        try {
            if (o instanceof IPorts) {
                IPorts ports = (IPorts) o;
                IHBCallback callback = ports.getCallback();
                if (callback != null) {
                    callback.remove(ports);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return super.remove(o);
    }
}
