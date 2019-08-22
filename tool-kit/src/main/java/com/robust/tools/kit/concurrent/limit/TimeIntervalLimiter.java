package com.robust.tools.kit.concurrent.limit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 间隔一定时间的限流器
 * @Author: robust
 * @CreateDate: 2019/8/1 10:26
 * @Version: 1.0
 */
public class TimeIntervalLimiter {

    private final AtomicLong lastTimeAtom = new AtomicLong(0);

    private long windowTimeMillis;

    public TimeIntervalLimiter(long interval, TimeUnit timeUnit) {
        this.windowTimeMillis = timeUnit.toMillis(interval);
    }

    public boolean tryAcquire() {
        long currentTime = System.currentTimeMillis();
        long lastTime = lastTimeAtom.get();
        /**
         * 当当前时间-上次成功时间>=窗口时间，且当前线程未发生竞争时，获取成功
         */
        return currentTime - lastTime >= windowTimeMillis && lastTimeAtom.compareAndSet(lastTime, currentTime);
    }
}
