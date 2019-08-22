package com.robust.tools.kit.concurrent;

import com.robust.tools.kit.concurrent.threadpool.QueuableCachedThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: copy from tomcat 8.5.x
 * <p>
 * https://github.com/apache/tomcat/blob/8.5.x/java/org/apache/tomcat/util/threads/TaskQueue.java
 * @Author: robust
 * @CreateDate: 2019/8/5 9:48
 * @Version: 1.0
 */
public class TaskQueue extends LinkedBlockingQueue<Runnable> {

    private static final long serialVersionUID = 7983389165001386578L;

    private transient volatile QueuableCachedThreadPool parent = null;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setParent(QueuableCachedThreadPool pool) {
        parent = pool;
    }

    public boolean force(Runnable runnable) {
        if (parent.isShutdown()) {
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue.");
        }
        return super.offer(runnable);// forces the item onto the queue, to be used if the task is rejected
    }

    public boolean force(Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException {
        if (parent.isShutdown()) {
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        }
        return super.offer(runnable, timeout, unit); // forces the item onto the queue, to be used if the task is rejected
    }

    public boolean offer(Runnable o) {
        /**
         * {@link ThreadPoolExecutor#getPoolSize()} 是个有锁操作，尽量减少
         */
        int currentPoolSize = parent.getPoolSize();
        //we are maxed out on threads, simply queue the object
        if (currentPoolSize >= parent.getMaximumPoolSize()) return super.offer(o);
        //we have idle threads, just add it to the queue
        if (parent.getSubmittedCount() <= currentPoolSize) return super.offer(o);
        //if we have less threads than maximum force creation of a new thread
        if (currentPoolSize < parent.getMaximumPoolSize()) return false;
        //if we reached here, we need to add it to the queue
        return super.offer(o);
    }
}
