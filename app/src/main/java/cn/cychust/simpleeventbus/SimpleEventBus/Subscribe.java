package cn.cychust.simpleeventbus.SimpleEventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
  int threadMode() default ThreadMode.POST_THREAD;  //默认在发送线程
}
