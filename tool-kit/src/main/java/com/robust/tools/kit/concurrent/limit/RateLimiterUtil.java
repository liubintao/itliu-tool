package com.robust.tools.kit.concurrent.limit;

import com.google.common.util.concurrent.RateLimiter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Description: 限流工具类
 * @Author: robust
 * @CreateDate: 2019/7/30 20:37
 * @Version: 1.0
 */
public class RateLimiterUtil {

    /**
     * 一个用来定制RateLimit的方法
     *
     * @param permitsPerSecond 每秒允许的请求数，可看成QPS(每秒查询率)
     * @param maxBurstSeconds  最大的突发缓冲时间，用来应对突发流量。Guava的实现默认是1s，permitsPerSecond * maxBurstSeconds,
     *                         就是闲置时预留的缓冲token数量
     * @return
     */
    public static RateLimiter create(double permitsPerSecond, double maxBurstSeconds)
            throws ReflectiveOperationException {
        return create(permitsPerSecond, maxBurstSeconds, true);
    }

    /**
     * 一个用来定制RateLimit的方法
     *
     * @param permitsPerSecond 每秒允许的请求数，可看成QPS(每秒查询率)
     * @param maxBurstSeconds  最大的突发缓冲时间，用来应对突发流量。Guava的实现默认是1s，permitsPerSecond * maxBurstSeconds,
     *                         就是闲置时预留的缓冲token数量
     * @param filledWithToken  是否需要创建时就保留有 permitsPerSecond * maxBurstSeconds 的token
     * @return
     */
    public static RateLimiter create(double permitsPerSecond, double maxBurstSeconds, boolean filledWithToken)
            throws ReflectiveOperationException {
        Class<?> sleepingStopwatch =
                Class.forName("com.google.common.util.concurrent.RateLimiter$SleepingStopwatch");
        Method createStopwatchMethod = sleepingStopwatch.getDeclaredMethod("createFromSystemTimer");
        createStopwatchMethod.setAccessible(true);
        Object stopWatch = createStopwatchMethod.invoke(sleepingStopwatch, null);

        Class<?> smoothBursty = Class.forName("com.google.common.util.concurrent.SmoothRateLimiter$SmoothBursty");
        Constructor smoothBurstyConstructor = smoothBursty.getDeclaredConstructors()[0];
        smoothBurstyConstructor.setAccessible(true);
        RateLimiter rateLimiter = (RateLimiter) smoothBurstyConstructor.newInstance(stopWatch, maxBurstSeconds);
        rateLimiter.setRate(permitsPerSecond);

        if (filledWithToken) {
            //set storedPermits
            setFiled(rateLimiter, "storedPermits", maxBurstSeconds * permitsPerSecond);
        }

        return rateLimiter;
    }

    private static boolean setFiled(Object object, String filedName, Object value) {
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(filedName);
        } catch (NoSuchFieldException e) {
            field = null;
        }
        Class superClass = object.getClass().getSuperclass();
        while (field == null && superClass != null) {
            try {
                field = superClass.getDeclaredField(filedName);
            } catch (NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
            }
        }
        if (field == null) {
            return false;
        }
        field.setAccessible(true);
        try {
            field.set(object, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
