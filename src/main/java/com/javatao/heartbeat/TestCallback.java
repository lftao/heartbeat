package com.javatao.heartbeat;

public class TestCallback implements IHBCallback{

    @Override
    public void remove(IPorts ports) {
        System.out.println("remove " + ports);
    }
    @Override
    public void add(IPorts ports) {
        System.out.println("add " + ports);
    }
    
}
