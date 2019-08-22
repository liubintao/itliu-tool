package com.robust.tools.kit.concurrent.threadpool;

import com.robust.tools.kit.concurrent.TaskQueue;
import org.junit.Test;

import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/5 17:59
 * @Version: 1.0
 */
public class ThreadPoolBuilderTest {
    @Test
    public void fixPool() {
        ThreadPoolExecutor fixedPool = ThreadPoolBuilder.fixedPool().build();
        assertThat(fixedPool.getCorePoolSize()).isEqualTo(1);
        assertThat(fixedPool.getMaximumPoolSize()).isEqualTo(1);
        assertThat(fixedPool.getQueue()).isInstanceOf(LinkedBlockingQueue.class);
        fixedPool.shutdown();

        fixedPool = ThreadPoolBuilder.fixedPool().setPoolSize(10).build();
        assertThat(fixedPool.getCorePoolSize()).isEqualTo(10);
        assertThat(fixedPool.getMaximumPoolSize()).isEqualTo(10);
        assertThat(fixedPool.getQueue()).isInstanceOf(LinkedBlockingQueue.class);
        fixedPool.shutdown();

        fixedPool = ThreadPoolBuilder.fixedPool().setPoolSize(10).setQueueSize(5).setThreadFactory(
                ThreadPoolUtil.buildThreadFactory("test")).build();
        assertThat(fixedPool.getCorePoolSize()).isEqualTo(10);
        assertThat(fixedPool.getMaximumPoolSize()).isEqualTo(10);
        Thread thread = fixedPool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("test");
        assertThat(thread.isDaemon()).isFalse();
        assertThat(fixedPool.getQueue()).isInstanceOf(ArrayBlockingQueue.class);
        fixedPool.shutdown();

        fixedPool = ThreadPoolBuilder.fixedPool().setThreadNamePrefix("pool").build();
        assertThat(fixedPool.getCorePoolSize()).isEqualTo(1);
        assertThat(fixedPool.getMaximumPoolSize()).isEqualTo(1);
        thread = fixedPool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("pool");
        assertThat(thread.isDaemon()).isFalse();
        assertThat(fixedPool.getQueue()).isInstanceOf(LinkedBlockingQueue.class);
        fixedPool.shutdown();

        fixedPool = ThreadPoolBuilder.fixedPool().setThreadNamePrefix("aaa").setDaemon(true).build();
        assertThat(fixedPool.getCorePoolSize()).isEqualTo(1);
        assertThat(fixedPool.getMaximumPoolSize()).isEqualTo(1);
        thread = fixedPool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("aaa");
        assertThat(thread.isDaemon()).isTrue();
        assertThat(fixedPool.getQueue()).isInstanceOf(LinkedBlockingQueue.class);
        fixedPool.shutdown();
    }

    @Test
    public void cachedPool() {
        ThreadPoolExecutor pool = ThreadPoolBuilder.cachedPool().build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(0);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(Integer.MAX_VALUE);
        assertThat(pool.getKeepAliveTime(TimeUnit.SECONDS)).isEqualTo(10);
        assertThat(pool.getQueue()).isInstanceOf(SynchronousQueue.class);
        pool.shutdown();

        pool = ThreadPoolBuilder.cachedPool().setMinSize(10).setMaxSize(100).setKeepAliveTime(15).build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(10);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(100);
        assertThat(pool.getKeepAliveTime(TimeUnit.SECONDS)).isEqualTo(15);
        assertThat(pool.getQueue()).isInstanceOf(SynchronousQueue.class);
        pool.shutdown();

        pool = ThreadPoolBuilder.cachedPool().setMinSize(10).setMaxSize(100).setKeepAliveTime(15)
                .setThreadFactory(ThreadPoolUtil.buildThreadFactory("POOL")).build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(10);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(100);
        assertThat(pool.getKeepAliveTime(TimeUnit.SECONDS)).isEqualTo(15);
        Thread thread = pool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("POOL");
        assertThat(pool.getQueue()).isInstanceOf(SynchronousQueue.class);
        pool.shutdown();
    }

    @Test
    public void schedulePool() {
        ScheduledThreadPoolExecutor pool = ThreadPoolBuilder.scheduledPool().build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(1);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(Integer.MAX_VALUE);
        pool.shutdown();

        pool = ThreadPoolBuilder.scheduledPool().setPoolSize(2).build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(2);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(Integer.MAX_VALUE);
        pool.shutdown();

        pool = ThreadPoolBuilder.scheduledPool().setThreadNamePrefix("scheduledPool").build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(1);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(Integer.MAX_VALUE);
        Thread thread = pool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("scheduledPool");
        pool.shutdown();
    }

    @Test
    public void queuableCachedPool() {
        QueuableCachedThreadPool pool = ThreadPoolBuilder.queuableCachedPool().build();
        assertThat(pool.getPoolSize()).isEqualTo(0);
        assertThat(pool.getCorePoolSize()).isEqualTo(0);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(Integer.MAX_VALUE);
        assertThat(pool.getKeepAliveTime(TimeUnit.SECONDS)).isEqualTo(10);
        assertThat(pool.getQueue()).isInstanceOf(TaskQueue.class);
        pool.shutdown();

        pool = ThreadPoolBuilder.queuableCachedPool().setMinSize(10).setMaxSize(100).setKeepAlive(15)
                .setQueueSize(150).setThreadFactory(ThreadPoolUtil.buildThreadFactory("queuable")).build();
        assertThat(pool.getPoolSize()).isEqualTo(10);
        assertThat(pool.getCorePoolSize()).isEqualTo(10);
        assertThat(pool.getMaximumPoolSize()).isEqualTo(100);
        assertThat(pool.getKeepAliveTime(TimeUnit.SECONDS)).isEqualTo(15);
        assertThat(pool.getQueue()).isInstanceOf(TaskQueue.class);
        Thread thread = pool.getThreadFactory().newThread(() -> {});
        assertThat(thread.getName()).startsWith("queuable");
        pool.shutdown();


    }
}