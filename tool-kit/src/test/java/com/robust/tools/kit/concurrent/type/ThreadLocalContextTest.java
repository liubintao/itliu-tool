package com.robust.tools.kit.concurrent.type;

import com.robust.tools.kit.concurrent.ConcurrentUtil;
import com.robust.tools.kit.number.RandomUtil;
import org.junit.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 19:15
 * @Version: 1.0
 */
public class ThreadLocalContextTest {

    @Test
    public void test() throws InterruptedException {
        CountDownLatch latch = ConcurrentUtil.countDownLatch(10);
        CyclicBarrier barrier = ConcurrentUtil.cyclicBarrier(10, () -> {
            System.out.println("start ...");
        });

        Runnable r = () -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            ThreadLocalContext.put("name", Thread.currentThread().getName());
            try {
                TimeUnit.MILLISECONDS.sleep(RandomUtil.nextLong(100, 300));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println((String) ThreadLocalContext.get("name"));
            latch.countDown();
        };

        for (int i = 0; i < 10; i++) {
            new Thread(r).start();
        }
        latch.await();
    }
}