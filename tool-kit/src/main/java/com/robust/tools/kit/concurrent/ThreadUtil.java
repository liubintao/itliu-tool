package com.robust.tools.kit.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程相关工具类
 * <p>
 * 1、处理了InterruptedException的sleep
 * <p>
 * 2、正确的InterruptedException处理方法
 * @Author: robust
 * @CreateDate: 2019/8/6 19:52
 * @Version: 1.0
 */
public class ThreadUtil {

    /**
     * sleep等待,单位为毫秒,已捕捉并处理InterruptedException.
     *
     * @param durationMillis 毫秒数
     */
    public static void sleep(long durationMillis) {
        try {
            TimeUnit.MILLISECONDS.sleep(durationMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * sleep等待,已捕捉并处理InterruptedException.
     *
     * @param duration 睡眠时间
     * @param timeUnit 睡眠时间单位
     */
    public static void sleep(long duration, TimeUnit timeUnit) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeUnit.toMillis(duration));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 纯粹为了提醒下处理InterruptedException的正确方式，除非你是在写不可中断的任务.
     */
    public static void handleInterruptedException() {
        Thread.currentThread().interrupt();
    }
}
