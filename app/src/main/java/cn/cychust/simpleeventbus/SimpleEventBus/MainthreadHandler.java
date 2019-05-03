package cn.cychust.simpleeventbus.SimpleEventBus;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.reflect.InvocationTargetException;

public class MainthreadHandler extends Handler {
    private Object       event;
    private Subscription mSubscription;

    public MainthreadHandler(Looper looper) {
        super(looper);
    }

    public void post(Subscription subscription, Object event) {
        this.mSubscription = subscription;
        this.event = event;
        sendMessage(Message.obtain());
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            mSubscription.mMethod.invoke(mSubscription.subscriber, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
