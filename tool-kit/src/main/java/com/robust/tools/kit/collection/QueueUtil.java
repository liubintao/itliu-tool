package com.robust.tools.kit.collection;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * @Description: Queue工具集
 * 各种Queue、Dequeue的创建
 * @Author: robust
 * @CreateDate: 2019/7/29 16:43
 * @Version: 1.0
 */
public class QueueUtil {

    /**
     * 创建ArrayDeque
     * <p>
     * 需设置初始长度，默认为16，数组满时成倍扩容
     *
     * @param initSize
     * @param <E>
     * @return
     */
    public static <E> ArrayDeque<E> newArrayDeque(int initSize) {
        return new ArrayDeque<>(initSize);
    }

    /**
     * 创建LinkedDeque(LinkedList实现了Deque)
     *
     * @param <E>
     * @return
     */
    public static <E> LinkedList<E> newLinkedDeque() {
        return new LinkedList<>();
    }

    /**
     * 创建无阻塞情况下性能最优的并发队列
     *
     * @param <E>
     * @return
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentNonBlockingQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    /**
     * 创建无阻塞情况下性能最优的并发双端队列
     *
     * @param <E>
     * @return
     */
    public static <E> ConcurrentLinkedDeque<E> newConcurrentNonBlockingDeque() {
        return new ConcurrentLinkedDeque();
    }

    /**
     * 创建一个并发阻塞情况下，长度不受限的队列
     * 长度不受限，即生产者不会因为队列满而阻塞，但消费者会因为队列空而阻塞
     *
     * @param <E>
     * @return
     */
    public static <E> LinkedBlockingQueue<E> newBlockingUnLimitQueue() {
        return new LinkedBlockingQueue<>();
    }

    /**
     * 创建一个并发阻塞情况下，长度不受限的双端队列
     * 长度不受限，即生产者不会因为队列满而阻塞，但消费者会因为队列空而阻塞
     *
     * @param <E>
     * @return
     */
    public static <E> LinkedBlockingDeque<E> newBlockingUnLimitDeque() {
        return new LinkedBlockingDeque<>();
    }

    /**
     * 创建并发阻塞情况下，长度受限的，更节约内存，但共用一把锁的队列
     *
     * @param capacity
     * @param <E>
     * @return
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
        return new ArrayBlockingQueue<>(capacity);
    }

    /**
     * 创建并发阻塞情况下，长度受限，队头队尾两把锁，但使用更多内存的队列
     *
     * @param capacity
     * @param <E>
     * @return
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
        return new LinkedBlockingQueue<>();
    }

    /**
     * 创建并发阻塞情况下，长度受限，队头队尾两把锁，但使用更多内存的双端队列
     *
     * @param capacity
     * @param <E>
     * @return
     */
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
        return new LinkedBlockingDeque<>();
    }
}
