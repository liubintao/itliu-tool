package com.robust.tools.kit.concurrent.threadpool;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 10:13
 * @Version: 1.0
 */
public class ThreadPoolUtilTest {
    @Test
    public void gracefulShutdown() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {});
        ThreadPoolUtil.gracefulShutdown(executorService, 1000);
        assertThat(executorService.isTerminated()).isTrue();

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {});
        ThreadPoolUtil.gracefulShutdown(executorService, 1000, TimeUnit.NANOSECONDS);
        TimeUnit.NANOSECONDS.sleep(1000);
        assertThat(executorService.isTerminated()).isTrue();
    }

    @Test
    public void buildThreadFactory() {
        ThreadFactory threadFactory = ThreadPoolUtil.buildThreadFactory("test");
        Thread thread = threadFactory.newThread(() -> {});

        assertThat(thread.getName()).startsWith("test");
        assertThat(thread.isDaemon()).isFalse();

        threadFactory = ThreadPoolUtil.buildThreadFactory("test", true);
        thread = threadFactory.newThread(() -> {});

        assertThat(thread.getName()).startsWith("test");
        assertThat(thread.isDaemon()).isTrue();
    }

    @Test
    public void safeRunnable() {
        Thread thread = new Thread(ThreadPoolUtil.safeRunnable(new WrapRunnable()));
        thread.start();
    }

    public static class WrapRunnable implements Runnable{

        @Override
        public void run() {
            throw new RuntimeException("aaa");
        }
    }
}