package com.robust.tools.kit.concurrent.threadpool;

import com.robust.tools.kit.concurrent.ThreadDumper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: 如果线程池已满，退出申请并打印Thread Dump(会有一定的最少间隔，默认为10分钟）
 * @Author: robust
 * @CreateDate: 2019/8/5 15:43
 * @Version: 1.0
 */
@Slf4j
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {

    private final String threadName;

    private ThreadDumper threadDumpper = new ThreadDumper();

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format(
                "Thread pool is EXHAUSTED!"
                        + " Thread name: %s, Pool size: %d (active: %d, core: %d, max: %d, largest: %d),"
                        + " Task: %d (completed: %d), Executor status: (isShutdown: %s, isTerminated: %s, " +
                        "isTerminating: %s)!", threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(),
                e.getMaximumPoolSize(), e.getLargestPoolSize(), e.getTaskCount(), e.getCompletedTaskCount(),
                e.isShutdown(), e.isTerminated(), e.isTerminating()
        );
        log.warn(msg);
        threadDumpper.tryThreadDump(null);
        throw new RejectedExecutionException(msg);
    }
}
