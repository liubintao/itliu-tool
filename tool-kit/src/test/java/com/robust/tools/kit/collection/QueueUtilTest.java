package com.robust.tools.kit.collection;

import com.google.common.collect.EvictingQueue;
import com.robust.tools.kit.collection.type.MoreQueues;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/29 17:22
 * @Version: 1.0
 */
public class QueueUtilTest {

    @Test
    public void build() {
        ArrayDeque<String> queue1 = QueueUtil.newArrayDeque(16);
        LinkedList<String> queue2 = QueueUtil.newLinkedDeque();

        ConcurrentLinkedQueue<String> queue3 = QueueUtil.newConcurrentNonBlockingQueue();
        Deque<String> queue7 = QueueUtil.newConcurrentNonBlockingDeque();

        LinkedBlockingQueue<String> queue4 = QueueUtil.newBlockingUnLimitQueue();
        LinkedBlockingDeque<String> queue8 = QueueUtil.newBlockingUnLimitDeque();

        LinkedBlockingQueue<String> queue5 = QueueUtil.newLinkedBlockingQueue(100);
        ArrayBlockingQueue<String> queue6 = QueueUtil.newArrayBlockingQueue(100);
        LinkedBlockingDeque<String> queue9 = QueueUtil.newLinkedBlockingDeque(100);
    }

    @Test
    public void stack() {

        Queue<String> stack = MoreQueues.createStack(10);
        Queue<String> stack2 = MoreQueues.createConcurrentStack();

        stack.offer("1");
        stack.offer("2");

        assertThat(stack.poll()).isEqualTo("2");
        assertThat(stack.poll()).isEqualTo("1");

        stack2.offer("1");
        stack2.offer("2");

        assertThat(stack2.poll()).isEqualTo("2");
        assertThat(stack2.poll()).isEqualTo("1");
    }

    @Test
    public void lru() {
        EvictingQueue queue = MoreQueues.createLRUQueue(2);
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        assertThat(queue.contains(1)).isFalse();
    }
}