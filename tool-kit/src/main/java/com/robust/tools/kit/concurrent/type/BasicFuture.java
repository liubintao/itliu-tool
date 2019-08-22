package com.robust.tools.kit.concurrent.type;

import com.robust.tools.kit.base.Validate;

import java.util.concurrent.*;

/**
 * @Description: 从Apache HttpClient 移植(2017.4)，一个Future实现类的基本框架.
 * https://github.com/apache/httpcomponents-core/blob/master/httpcore5/src/main/java/org/apache/hc/core5/concurrent/BasicFuture.java
 * <p>
 * 不过HC用的是callback，这里用的是继承
 * @Author: robust
 * @CreateDate: 2019/8/6 11:13
 * @Version: 1.0
 */
public abstract class BasicFuture<T> implements Future<T> {

    private volatile boolean completed;
    private volatile boolean cancelled;
    private volatile T result;
    private volatile Exception ex;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        synchronized (this) {
            if(this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;
            notifyAll();
        }
        onCancelled();
        return true;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return this.completed;
    }

    @Override
    public synchronized T get() throws InterruptedException, ExecutionException {
        if (!this.completed) {
            wait();
        }
        return getResult();
    }

    @Override
    public synchronized T get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        Validate.notNull(unit);
        long millions = unit.toMillis(timeout);
        long startTime = millions <= 0 ? 0 : System.currentTimeMillis();
        long waitTime = millions;
        if (this.completed) {
            return getResult();
        } else if (waitTime <= 0) {
            throw new TimeoutException();
        } else {
            for (; ; ) {
                wait(waitTime);
                if (this.completed) {
                    return getResult();
                } else {
                    waitTime = millions - (System.currentTimeMillis() - startTime);
                    if (waitTime <= 0) {
                        throw new TimeoutException();
                    }
                }
            }
        }
    }

    private T getResult() throws ExecutionException {
        if (this.ex != null) {
            throw new ExecutionException(this.ex);
        }

        if (this.cancelled) {
            throw new CancellationException();
        }

        return this.result;
    }

    public boolean completed(final T result) {
        synchronized (this) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.result = result;
            notifyAll();
        }
        onCompleted(result);
        return true;
    }

    public boolean failed(final Exception ex) {
        synchronized (this) {
            if(this.completed) {
                return false;
            }
            this.completed = true;
            this.ex = ex;
            notifyAll();
        }
        onFailed(ex);
        return true;
    }

    protected abstract void onCompleted(T result);

    protected abstract void onFailed(Exception ex);

    protected abstract void onCancelled();
}
