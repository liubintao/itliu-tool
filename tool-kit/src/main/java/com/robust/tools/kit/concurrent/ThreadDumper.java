package com.robust.tools.kit.concurrent;

import com.robust.tools.kit.concurrent.limit.TimeIntervalLimiter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ThreadInfo;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 由程序触发的ThreadDump, 打印到日志中.
 * <p>
 * 因为ThreadDump本身会造成JVM停顿,所以加上了开关和最少间隔时间的限制(默认不限制)
 * 因为{@link ThreadInfo#toString()}最多只会打印8层StackTrace,所以加上了最大打印层数的选项.(默认为8)
 * @Author: robust
 * @CreateDate: 2019/8/5 15:51
 * @Version: 1.0
 */
@Slf4j
public class ThreadDumper {
    private static final int DEFAULT_MAX_STACK_LEVEL = 8;
    private static final int DEFAULT_MIN_INTERVAL = 1000 * 60 * 10;//10分钟
    //打印StackTrace的最大深度
    @Setter
    private int maxStackLevel;
    private TimeIntervalLimiter timeIntervalLimiter;

    public ThreadDumper() {
        this(DEFAULT_MIN_INTERVAL, DEFAULT_MAX_STACK_LEVEL);
    }

    public ThreadDumper(long leastIntervalMills, int maxStackLevel) {
        this.maxStackLevel = maxStackLevel;
        this.timeIntervalLimiter = new TimeIntervalLimiter(leastIntervalMills, TimeUnit.MILLISECONDS);
    }

    /**
     * 符合条件则打印线程栈
     */
    public void tryThreadDump() {
        tryThreadDump(null);
    }

    public void setLeastInterval(int leastInterval) {
        this.timeIntervalLimiter = new TimeIntervalLimiter(leastInterval, TimeUnit.MILLISECONDS);
    }


    /**
     * @param reasonMsg 发生ThreadDump的原因
     */
    public void tryThreadDump(String reasonMsg) {
        if (timeIntervalLimiter.tryAcquire()) {
            threadDump(reasonMsg);
        }
    }

    /**
     * 强行打印ThreadDump，使用最轻量的采集方式，不打印锁信息
     *
     * @param reasonMsg 发生ThreadDump的原因
     */
    private void threadDump(String reasonMsg) {
        log.info("Thread dump by ThreadDumper" + reasonMsg != null ? (" for " + reasonMsg) : "");
        Map<Thread, StackTraceElement[]> threadMap = Thread.getAllStackTraces();
        // 两条日志间的时间间隔，是VM被thread dump堵塞的时间.
        log.info("Finish the threads snapshot");

        StringBuilder sb = new StringBuilder(8192 * 20).append('\n');
        for (Map.Entry<Thread, StackTraceElement[]> entry : threadMap.entrySet()) {
            dumpThreadInfo(entry.getKey(), entry.getValue(), sb);
        }
        log.info(sb.toString());
    }

    private String dumpThreadInfo(Thread thread, StackTraceElement[] stackTraceElements, StringBuilder sb) {
        sb.append('\"').append(thread.getName()).append("\" Id=").append(thread.getId()).append(' ')
                .append(thread.getState());
        sb.append('\n');
        int i = 0;
        for (; i < Math.min(maxStackLevel, stackTraceElements.length); i++) {
            StackTraceElement se = stackTraceElements[i];
            sb.append("\tat ").append(se).append('\n');
        }
        if (i < stackTraceElements.length) {
            sb.append("\t...").append('\n');
        }
        sb.append('\n');
        return sb.toString();
    }
}
