package com.robust.tools.kit.concurrent;

import com.google.common.util.concurrent.RateLimiter;
import com.robust.tools.kit.concurrent.limit.RateLimiterUtil;
import com.robust.tools.kit.concurrent.limit.Sampler;
import com.robust.tools.kit.concurrent.limit.TimeIntervalLimiter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description: 并发工具类
 * @Author: robust
 * @CreateDate: 2019/7/30 19:27
 * @Version: 1.0
 */
public class ConcurrentUtil {
    /**
     * 返回没有激烈CAS冲突的LongAdder,并发的+1将在不同的Counter里进行,只是在取值时将多个Counter求和.
     *
     * @return
     */
    public static LongAdder longAdder() {
        return new LongAdder();
    }

    /**
     * 返回CountDownLatch(闭锁),每条线程执行{@link CountDownLatch#countDown()}-1,减到0时,正在{@link CountDownLatch#await()}的线程
     * 继续执行
     *
     * @param count
     * @return
     */
    public static CountDownLatch countDownLatch(int count) {
        return new CountDownLatch(count);
    }

    /**
     * 返回CyclicBarrier(栅栏屏障),每条线程执行到{@link CyclicBarrier#await()}时执行--count操作,当count为0时,所有阻塞在{@link CyclicBarrier#await()}
     * 处的线程继续运行
     *
     * @param count
     * @return
     */
    public static CyclicBarrier cyclicBarrier(int count) {
        return new CyclicBarrier(count);
    }
    /**
     * 返回CyclicBarrier(栅栏屏障),每条线程执行到{@link CyclicBarrier#await()}时执行--count操作,当count为0时,所有阻塞在{@link CyclicBarrier#await()}
     * 处的线程继续运行,并且会触发runnable内的操作
     *
     * @param count 阻塞线程数
     * @param runnable 所有阻塞的线程继续执行时触发的操作
     * @return
     */
    public static CyclicBarrier cyclicBarrier(int count, Runnable runnable) {
        return new CyclicBarrier(count, runnable);
    }

    /**
     * 返回默认非公平的Semaphore(信号量),先请求的线程不一定先拿到信号量
     *
     * @param permits
     * @return
     */
    public static Semaphore nonFairSemaphore(int permits) {
        return new Semaphore(permits);
    }

    /**
     * 返回公平的Semaphore(信号量),先请求的线程先拿到信号量
     *
     * @param permits
     * @return
     */
    public static Semaphore fairSemaphore(int permits) {
        return new Semaphore(permits, true);
    }

    /*-----------------限流采样----------------------------*/

    /**
     * 返回令牌桶算法的RateLimit默认版,默认令牌桶大小等于期望的QPS,且刚启动时桶为空
     *
     * @param permitsPerSecond 每秒允许的请求数,可看成QPS,同时将QPS平滑移动到毫秒级别上,请求到达速度不平滑时依赖缓冲能力.
     * @return
     */
    public static RateLimiter rateLimiter(int permitsPerSecond) {
        return RateLimiter.create(permitsPerSecond);
    }

    /**
     * 返回令牌桶算法的RateLimiter定制版，可定制令牌桶的大小，且刚启动时桶已装满。
     *
     * @param permitsPerSecond 每秒允许的请求数，可看成QPS，同时将QPS平滑到毫秒级别上，请求到达速度不平滑时依赖缓冲能力.
     * @param maxBurstSeconds  可看成桶的容量，Guava中最大的突发流量缓冲时间，默认是1s, permitsPerSecond * maxBurstSeconds，就是闲时能累积的缓冲token最大数量。
     */
    public static RateLimiter rateLimiter(int permitsPerSecond, int maxBurstSeconds) throws ReflectiveOperationException {
        return RateLimiterUtil.create(permitsPerSecond, maxBurstSeconds);
    }

    /**
     * 返回采样器.
     *
     * @param selectPercent 采样率，可以有小数点，0-100之间.
     * @return
     */
    public static Sampler sampler(double selectPercent) {
        return Sampler.create(selectPercent);
    }

    /**
     * 返回时间间隔限流器
     *
     * @param interval 间隔时间
     * @param timeUnit 时间单位
     * @return
     */
    public static TimeIntervalLimiter timeIntervalLimiter(long interval, TimeUnit timeUnit) {
        return new TimeIntervalLimiter(interval, timeUnit);
    }
}
