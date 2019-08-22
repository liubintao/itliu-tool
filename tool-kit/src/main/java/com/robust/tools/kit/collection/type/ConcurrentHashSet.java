package com.robust.tools.kit.collection.type;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @Description: JDK没有提供ConcurrentHashSet, 考虑到JDK的HashSet是基于HashMap的，因此ConcurrentHashSet也基于ConcurrentHashMap完成.
 * 虽然可以通过{@link Collections#newSetFromMap(Map)} 构建,但声明一个单独的类型，阅读代码时能更清晰的知道set的并发友好性,代码来自JDK的newSetFromMap.
 * @Author: robust
 * @CreateDate: 2019/7/29 11:57
 * @Version: 1.0
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable {
    private final Map<E, Boolean> m;  // The backing map
    private transient Set<E> s;       // Its keySet

    public ConcurrentHashSet() {
        this.m = new ConcurrentHashMap<>();
        this.s = m.keySet();
    }

    public void clear() {
        m.clear();
    }

    public int size() {
        return m.size();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean contains(Object o) {
        return m.containsKey(o);
    }

    public boolean remove(Object o) {
        return m.remove(o) != null;
    }

    public boolean add(E e) {
        return m.put(e, Boolean.TRUE) == null;
    }

    public Iterator<E> iterator() {
        return s.iterator();
    }

    public Object[] toArray() {
        return s.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    public String toString() {
        return s.toString();
    }

    public int hashCode() {
        return s.hashCode();
    }

    public boolean equals(Object o) {
        return o == this || s.equals(o);
    }

    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }
    // addAll is the only inherited implementation

    // Override default methods in Collection
    @Override
    public void forEach(Consumer<? super E> action) {
        s.forEach(action);
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return s.removeIf(filter);
    }

    @Override
    public Spliterator<E> spliterator() {
        return s.spliterator();
    }

    @Override
    public Stream<E> stream() {
        return s.stream();
    }

    @Override
    public Stream<E> parallelStream() {
        return s.parallelStream();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        s = m.keySet();
    }
}
