package cn.cychust.simpleeventbus.SimpleEventBus;

import java.lang.reflect.Method;

public class Subscription {

    public int    mode;
    public Method mMethod;
    public Object subscriber;

    public Subscription(Method method, Object subscriber, int mode) {
        this.mode = mode;
        this.mMethod = method;
        this.subscriber = subscriber;
    }

}
