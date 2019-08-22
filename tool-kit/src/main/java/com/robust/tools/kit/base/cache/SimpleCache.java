package com.robust.tools.kit.base.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: 简单缓存，无超时实现，使用{@link java.util.WeakHashMap}实现缓存自动清理
 * @Author: robust
 * @CreateDate: 2019/7/17 12:00
 * @Version: 1.0
 */
public class SimpleCache<K, V> {

    /**
     * 缓存池
     */
    private final Map<K, V> cache = new WeakHashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    /**
     * 从缓存获取值
     * @param key
     * @return
     */
    public V get(K key) {
        readLock.lock();
        V val;
        try {
            val = cache.get(key);
        } finally {
            readLock.unlock();
        }
        return val;
    }

    /**
     * 放入缓存
     * @param key
     * @param val
     * @return
     */
    public V put(K key, V val) {
        writeLock.lock();
        try {
            cache.put(key, val);
        } finally {
            writeLock.unlock();
        }
        return val;
    }

    /**
     * 移除缓存
     * @param key
     * @return
     */
    private V remove(K key) {
        writeLock.lock();
        try {
            return cache.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清空缓存
     */
    private void clear() {
        writeLock.lock();
        try {
            cache.clear();
        } finally {
            writeLock.unlock();
        }
    }
}
