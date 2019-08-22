package com.robust.tools.kit.collection;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Ordering;
import com.robust.tools.kit.base.type.Pair;

import java.util.*;

/**
 * @Description: 通用Collection工具集
 * <p>
 * 1、集合是否为空，取得集合中首个及最后一个元素，判断集合是否完全相等
 * 2、集合的最大最小值，Top N，Bottom N
 * <p>
 * 另JDK中缺少ComparableComparator和NullComparator，直到JDK8才补上。
 * 因此平时请使用guava的Ordering.natural(),fluentable的API更好用，可以链式设置nullFirst，nullLast,reverse
 * @Author: robust
 * @CreateDate: 2019/7/24 15:43
 * @Version: 1.0
 * @see com.google.common.collect.Ordering
 */
public class CollectionUtil {
    /**
     * 是否为空
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 是否不为空
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * 取得集合中的第一个元素，若集合为空则返回null.
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T getFirst(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(0);
        }
        return collection.iterator().next();
    }

    /**
     * 获取集合中的最后一个元素，若集合为空则返回null.
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T getLast(Collection<T> collection) {
        if (collection == null) {
            return null;
        }
        if (collection instanceof List) {
            return ((List<T>) collection).get(collection.size() - 1);
        }
        return Iterators.getLast(collection.iterator());
    }

    /**
     * 两个集合中的所有元素按顺序相等.
     *
     * @param iterable1
     * @param iterable2
     * @param <T>
     * @return
     */
    public static <T> boolean elementEquals(Iterable<T> iterable1, Iterable<T> iterable2) {
        return Iterables.elementsEqual(iterable1, iterable2);
    }

    /*----------------------求最大值最小值，Top N，Bottom N-------------------------------*/

    /**
     * 返回无序集合中的最小值，使用元素默认排序
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> collection) {
        return Collections.min(collection);
    }

    /**
     * 返回无序集合中的最小值
     *
     * @param coll
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T> T min(Collection<? extends T> coll, Comparator<? super T> comparator) {
        return Collections.min(coll, comparator);
    }

    /**
     * 返回无序集合中的最大值，使用元素默认排序
     *
     * @param coll
     * @param <T>
     * @return
     */
    public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll) {
        return Collections.max(coll);
    }

    /**
     * 返回无序集合中的最大值
     *
     * @param coll
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> T max(Collection<? extends T> coll, Comparator<? super T> comp) {
        return Collections.max(coll, comp);
    }

    /**
     * 同时返回无序集合中最小值和最大值，使用元素默认排序
     * 在返回的Pair中，第一个元素为最小值，第二个元素为最大值
     *
     * @param coll
     * @param <T>
     * @return
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll) {
        Iterator<? extends T> iterator = coll.iterator();
        T minCandidate = iterator.next();
        T maxCandidate = minCandidate;
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next.compareTo(minCandidate) < 0) {
                minCandidate = next;
            } else if (next.compareTo(maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 同时返回无序集合中最小值和最大值，使用元素默认排序
     * 在返回的Pair中，第一个元素为最小值，第二个元素为最大值
     *
     * @param coll
     * @param comp
     * @param <T>
     * @return
     */
    public static <T extends Object & Comparable<? super T>> Pair<T, T> minAndMax(Collection<? extends T> coll, Comparator<? super T> comp) {
        Iterator<? extends T> iterator = coll.iterator();
        T minCandidate = iterator.next();
        T maxCandidate = minCandidate;
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (comp.compare(next, minCandidate) < 0) {
                minCandidate = next;
            } else if (comp.compare(next, maxCandidate) > 0) {
                maxCandidate = next;
            }
        }
        return Pair.of(minCandidate, maxCandidate);
    }

    /**
     * 返回Iterable中最大的N个对象, 最终调用guava的工具类完成
     *
     * @param coll
     * @param num
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> List<T> topN(Iterable<T> coll, int num) {
        return Ordering.natural().greatestOf(coll, num);
    }

    /**
     * 返回Iterable中最大的N个对象, 最终调用guava的工具类完成
     *
     * @param coll
     * @param num
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> List<T> topN(Iterable<T> coll, int num, Comparator<? super T> comp) {
        return Ordering.from(comp).greatestOf(coll, num);
    }

    /**
     * 返回Iterable中最小的N个对象, 最终调用guava的工具类完成
     *
     * @param coll
     * @param num
     * @param <T>
     * @return
     */
    public static <T extends Comparable<?>> List<T> bottomN(Iterable<T> coll, int num) {
        return Ordering.natural().leastOf(coll, num);
    }

    /**
     * 返回Iterable中最大的N个对象, 最终调用guava的工具类完成
     *
     * @param coll
     * @param num
     * @param comp
     * @param <T>
     * @return
     */
    public static <T> List<T> bottomN(Iterable<T> coll, int num, Comparator<? super T> comp) {
        return Ordering.from(comp).leastOf(coll, num);
    }
}
