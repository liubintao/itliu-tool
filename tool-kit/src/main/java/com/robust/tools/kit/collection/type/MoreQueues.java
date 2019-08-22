package com.robust.tools.kit.collection.type;

import com.google.common.collect.EvictingQueue;
import com.robust.tools.kit.collection.QueueUtil;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Queue;

/**
 * @Description: 特殊类型Queue
 * LIFO的Stack, LRU的Queue
 * <p>
 * @Author: robust
 * @CreateDate: 2019/7/30 15:37
 * @Version: 1.0
 */
public class MoreQueues {

    /*------------------特殊类型Queue：Stack-------------------*/

    /**
     * 支持后进先出的栈，用ArrayDeque实现，经过{@link Collections#asLifoQueue(Deque)}转换顺序
     * 需设置初始长度，默认为16，数组满时成倍扩容.
     *
     * @param initSize
     * @param <E>
     * @return
     */
    public static <E> Queue<E> createStack(int initSize) {
        return Collections.asLifoQueue(new ArrayDeque<>(initSize));
    }

    /**
     * 支持后进先出的并发无阻塞的栈，用ConcurrentLinkedDeque实现，经过{@link Collections#asLifoQueue(Deque)}转换顺序.
     * 另对于BlockingQueue接口， JDK暂无Lifo倒转实现，因此只能直接使用未调转顺序的LinkedBlockingDeque
     *
     * @param <E>
     * @return
     */
    public static <E> Queue<E> createConcurrentStack() {
        return Collections.asLifoQueue(QueueUtil.newConcurrentNonBlockingDeque());
    }

    /*------------------特殊类型Queue：LRUQueue-------------------*/

    /**
     * LRUQueue,如果Queue已满，删除最旧的元素
     * 内部实现是ArrayDeque
     *
     * @param maxSize
     * @param <E>
     * @return
     */
    public static <E> EvictingQueue<E> createLRUQueue(int maxSize) {
        return EvictingQueue.create(maxSize);
    }
}
