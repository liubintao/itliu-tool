package com.robust.tools.kit.collection;

import com.google.common.collect.Sets;
import com.robust.tools.kit.base.annotation.Nullable;
import com.robust.tools.kit.collection.type.ConcurrentHashSet;

import java.util.*;

/**
 * @Description: Set工具类
 * 1、包含集合的创建，各种Set和ConcurrentHashSet
 * 2、集合运算函数(交集，并集等, from guava)
 * @Author: robust
 * @CreateDate: 2019/7/18 19:28
 * @Version: 1.0
 */
public class SetUtil {

    /**
     * 根据等号左边的类型，构造类型正确的HashSet
     *
     * @param <E>
     * @return
     */
    public static <E> HashSet<E> newHashSet() {
        return Sets.newHashSet();
    }

    /**
     * 根据等号左边的类型，构造类型正确的HashSet, 并初始化元素.
     *
     * @see com.google.common.collect.Sets#newHashSet(Object...)
     */
    @SuppressWarnings("unchecked")
    public static <E> HashSet<E> newHashSet(E... elements) {
        return Sets.newHashSet(elements);
    }

    /**
     * HashSet涉及HashMap大小，因此建议在构造时传入需要初始的集合，其他如TreeSet不需要.
     *
     * @param iterable
     * @param <E>
     * @return
     * @see Sets#newHashSet(Iterable)
     */
    public static <E> HashSet<E> newHashSet(Iterable<? extends E> iterable) {
        return Sets.newHashSet(iterable);
    }

    /**
     * 创建HashSet并设置初始大小，因为HashSet内部是HashMap，会计算LoadFactor后的真实大小.
     *
     * @param capacity
     * @param <E>
     * @return
     * @see Sets#newHashSetWithExpectedSize(int)
     */
    public static <E> HashSet<E> newHashWithCapacity(int capacity) {
        return Sets.newHashSetWithExpectedSize(capacity);
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeSet, 通过实现了Comparable的元素自身进行排序.
     *
     * @param <E>
     * @return
     */
    public static <E extends Comparable> TreeSet<E> newTreeSet() {
        return new TreeSet<>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeSet, 并设置comparator.
     *
     * @param comparator
     * @param <E>
     * @return
     * @see Sets#newTreeSet(Comparator)
     */
    public static <E> TreeSet<E> newTreeSet(@Nullable Comparator<? super E> comparator) {
        return Sets.newTreeSet(comparator);
    }

    /**
     * 根据等号左边的类型,构造类型正确的TreeSet,并根据参数集合初始化.
     *
     * @param elements
     * @param <E>
     * @return
     * @see Sets#newTreeSet(Iterable)
     */
    public static <E extends Comparable> TreeSet<E> newTreeSet(Iterable<? extends E> elements) {
        return Sets.newTreeSet(elements);
    }

    /**
     * 根据等号左边的类型,构造类型正确的ConcurrentHashSet
     *
     * @param <E>
     * @return
     */
    public static <E> ConcurrentHashSet<E> newConcurrentHashSet() {
        return new ConcurrentHashSet();
    }

    /*-----------------from JDK Collections的常用构造函数-----------------------------------*/

    /**
     * 返回一个空的Set，节约空间
     * 注意此Set不可写，写入会抛出UnsupportedOperationException.
     *
     * @param <E>
     * @return
     * @see Collections#emptySet()
     */
    public static final <E> Set<E> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 如果set为null，转化为一个安全的空Set.
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     *
     * @see java.util.Collections#emptySet()
     */
    public static <E> Set<E> emptySetIfNull(final Set<E> set) {
        return set == null ? (Set<E>) Collections.EMPTY_SET : set;
    }

    /**
     * 返回只含一个元素但结构特殊的Set，节约空间.
     * <p>
     * 注意返回的Set不可写, 写入会抛出UnsupportedOperationException.
     *
     * @see java.util.Collections#singleton(Object)
     */
    public static final <E> Set<E> singletonSet(E o) {
        return Collections.singleton(o);
    }

    /**
     * 返回包装后不可修改的Set.
     * <p>
     * 如果尝试修改，会抛出UnsupportedOperationException
     *
     * @see java.util.Collections#unmodifiableSet(Set)
     */
    public static <E> Set<E> unmodifiableSet(Set<? extends E> s) {
        return Collections.unmodifiableSet(s);
    }

    /**
     * 从Map构造Set的大杀器, 可以用来制造各种Set
     *
     * @see java.util.Collections#newSetFromMap(Map)
     */
    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    /*-------------------------- from guava的集合运算函数------------------------*/

    /**
     * set1, set2的并集（在set1或set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     */
    public static <E> Set<E> unionView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.union(set1, set2);
    }

    /**
     * set1, set2的交集（同时在set1和set2的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     */
    public static <E> Set<E> intersectionView(final Set<E> set1, final Set<?> set2) {
        return Sets.intersection(set1, set2);
    }

    /**
     * set1, set2的差集（在set1，不在set2中的对象）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     */
    public static <E> Set<E> differenceView(final Set<E> set1, final Set<?> set2) {
        return Sets.difference(set1, set2);
    }

    /**
     * set1, set2的补集（在set1或set2中，但不在交集中的对象，又叫反交集）的只读view，不复制产生新的Set对象.
     * <p>
     * 如果尝试写入该View会抛出UnsupportedOperationException
     */
    public static <E> Set<E> disjointView(final Set<? extends E> set1, final Set<? extends E> set2) {
        return Sets.symmetricDifference(set1, set2);
    }
}
