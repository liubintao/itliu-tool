package com.robust.tools.kit.concurrent.threadpool;

import com.robust.tools.kit.concurrent.TaskQueue;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: copy from tomcat 8.5.x,传统的FixedThreadPool有Queue但线程数量不变，而CachedThreadPool线程数可变但没有Queue
 * <p>
 * Tomcat的线程池，通过控制TaskQueue，线程数，但线程数到达最大时会进入Queue中.
 * 代码从Tomcat复制，主要修改包括：
 * <p>
 * 1. 删除定期重启线程避免内存泄漏的功能，
 * <p>
 * 2. TaskQueue中可能3次有锁的读取线程数量，改为只读取1次，这把锁也是这个实现里的唯一遗憾了。
 * <p>
 * https://github.com/apache/tomcat/blob/8.5.x/java/org/apache/tomcat/util/threads/ThreadPoolExecutor.java
 * @Author: robust
 * @CreateDate: 2019/8/5 10:06
 * @Version: 1.0
 */
public class QueuableCachedThreadPool extends ThreadPoolExecutor {

    /**
     * The number of tasks submitted but not yet finished. This includes tasks
     * in the queue and tasks that have been handed to a worker thread but the
     * latter did not start executing the task yet.
     * This number is always greater or equal to {@link #getActiveCount()}.
     */
    private final AtomicInteger submittedCount = new AtomicInteger(0);

    public QueuableCachedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                    TaskQueue workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedHandler);
        workQueue.setParent(this);
        prestartAllCoreThreads();
    }


    public int getSubmittedCount() {
        return submittedCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        submittedCount.decrementAndGet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Runnable command) {
        execute(command, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes the given command at some time in the future. The command may execute in a new thread, in a pooled
     * thread, or in the calling thread, at the discretion of the <tt>Executor</tt> implementation. If no threads are
     * available, it will be added to the work queue. If the work queue is full, the system will wait for the specified
     * time and it throw a RejectedExecutionException if the queue is still full after that.
     *
     * @param command the runnable task
     * @param timeout A timeout for the completion of the task
     * @param unit    The timeout time unit
     * @throws RejectedExecutionException if this task cannot be accepted for execution - the queue is full
     * @throws NullPointerException       if command or unit is null
     */
    public void execute(Runnable command, long timeout, TimeUnit unit) {
        submittedCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) { // NOSONAR
            // not to re-throw this exception because this is only used to find out whether the pool is full, not for a
            // exception purpose
            final TaskQueue queue = (TaskQueue) super.getQueue();
            try {
                if (!queue.force(command, timeout, unit)) {
                    submittedCount.decrementAndGet();
                    throw new RejectedExecutionException("Queue capacity is full.");
                }
            } catch (InterruptedException ignore) {
                submittedCount.decrementAndGet();
                throw new RejectedExecutionException(ignore);
            }
        }
    }
}
