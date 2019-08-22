package com.robust.tools.kit.concurrent.threadpool;

import org.junit.Test;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/5 17:40
 * @Version: 1.0
 */
public class QueuableCachedThreadPoolTest {

    public static class RunTask implements Runnable {
        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    public void test() {
        QueuableCachedThreadPool pool = null;

        try {
            pool = ThreadPoolBuilder.queuableCachedPool().setMaxSize(0).setMaxSize(10).setQueueSize(10).build();
            for (int i = 0; i < 10; i++) {
                //线程满
                pool.submit(new RunTask());
            }
            assertThat(pool.getActiveCount()).isEqualTo(10);
            assertThat(pool.getQueue().size()).isEqualTo(0);

            // queue 满
            for (int i = 0; i < 10; i++) {
                pool.submit(new RunTask());
            }
            assertThat(pool.getActiveCount()).isEqualTo(10);
            assertThat(pool.getQueue().size()).isEqualTo(10);

            // 爆表
            try {
                pool.submit(new RunTask());
                fail("should fail before");
            } catch (Throwable t) {
                assertThat(t).isInstanceOf(RejectedExecutionException.class);
            }
        } finally {
            ThreadPoolUtil.gracefulShutdown(pool, 1000);
        }
    }
}