package com.robust.tools.kit.concurrent;

import com.robust.tools.kit.concurrent.threadpool.ThreadPoolBuilder;
import com.robust.tools.kit.concurrent.threadpool.ThreadPoolUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 10:59
 * @Version: 1.0
 */
public class ThreadDumperTest {

    private static class RunTask implements Runnable {

        private CountDownLatch latch;

        public RunTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            latch.countDown();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        ThreadPoolExecutor pool = ThreadPoolBuilder.fixedPool().setPoolSize(10).build();
        CountDownLatch latch = ConcurrentUtil.countDownLatch(10);
        for (int i = 0; i < 10; i++) {
            pool.submit(new RunTask(latch));
        }
        latch.await();

        ThreadDumper dumper = new ThreadDumper();
        dumper.tryThreadDump();

        dumper.setLeastInterval(2000);
        // 重置间隔会重置上一次写日志的时间,因此要调一次把新增的次数用完
        dumper.tryThreadDump();

        dumper.tryThreadDump();
        pool.shutdown();
    }
}