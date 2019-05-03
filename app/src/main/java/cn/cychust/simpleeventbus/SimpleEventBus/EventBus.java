package cn.cychust.simpleeventbus.SimpleEventBus;

import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private volatile static EventBus instance;

    private MainthreadHandler                 mHandler;
    private Map<Class<?>, List<Subscription>> mMap;

    private EventBus() {
        mMap = new HashMap<>();
        mHandler = new MainthreadHandler(Looper.getMainLooper()); //传入主looper
    }

    public static EventBus getDefault() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    public void register(Object subscriber) {
        Class<?> clazz   = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            //如果这个方法加上了Subscribe注解，则把它加入集合，并保存在map里面
            if (m.isAnnotationPresent(Subscribe.class)) {
                Subscribe          s    = m.getAnnotation(Subscribe.class);
                Class<?>           c    = m.getParameterTypes()[0];
                List<Subscription> list = mMap.get(c);
                if (list == null) {
                    list = new ArrayList<>();
                    mMap.put(c, list);
                }
                //根据threadMode构造订阅者
                switch (s.threadMode()) {
                    case ThreadMode.POST_THREAD:
                        list.add(new Subscription(m, subscriber,
                                                  ThreadMode.POST_THREAD));
                        break;
                    case ThreadMode.MAIN_THREAD:
                        list.add(new Subscription(m, subscriber,
                                                  ThreadMode.MAIN_THREAD));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void unRegister(Object subscriber) {
        Class<?> clazz   = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                Class<?>           c    = m.getParameterTypes()[0];
                List<Subscription> list = mMap.get(c);
                if (list != null) {
                    for (Subscription s : list) {
                        if (s.subscriber == subscriber) {
                            list.remove(s);
                        }
                    }
                }
            }
        }
    }


    public void post(Object event) {
        Class<?>           clazz = event.getClass();
        List<Subscription> list  = mMap.get(clazz);
        if (list == null) {
            return;
        }
        for (Subscription s : list) {
            switch (s.mode) {
                case ThreadMode.POST_THREAD:
                    try {
                        s.mMethod.invoke(s.subscriber, event);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    break;
                case ThreadMode.MAIN_THREAD:
                    mHandler.post(s, event);
                    break;
                default:
                    break;
            }
        }
    }
}
