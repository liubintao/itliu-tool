package com.robust.tools.kit.collection;

import com.google.common.collect.Lists;
import com.robust.tools.kit.lang.Filter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Description: 关于List的工具集合
 * 1、常用函数(如：是否为空)
 * @Author: robust
 * @CreateDate: 2019/7/18 20:00
 * @Version: 1.0
 */
public class ListUtil {

    /**
     * 判断集合是否为空, sort/
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
        return null == list || list.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List<?> list) {
        return null != list && !list.isEmpty();
    }

    /**
     * 获取集合的第一个元素，如果集合为空则返回<code>null</code>
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E getFirst(List<E> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取集合的最后一个元素,如果为空则返回<code>null</code>
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E getLast(List<E> list) {
        if (isEmpty(list)) {
            return null;
        }

        return list.get(list.size() - 1);
    }

    //--------------------from Guava的构造函数---------------------

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList
     * @param <E>
     * @return
     */
    public static <E> ArrayList<E> newArrayList() {
        return Lists.newArrayList();
    }

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList, 并初始化元素.
     *
     * @see com.google.common.collect.Lists#newArrayList(Object...)
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * 根据等号左边的类型，构造类型正确的ArrayList，并初始化元素
     *
     * @param elements
     * @param <E>
     * @return
     * @see com.google.common.collect.Lists#newArrayList(Iterable)
     */
    public static <E> ArrayList<E> newArrayList(Iterable<E> elements) {
        return Lists.newArrayList(elements);
    }

    /**
     * 根据等号左边的类型，构造类型正确的LinkedList，并初始化元素
     *
     * @param elements
     * @param <E>
     * @return
     * @see com.google.common.collect.Lists#newLinkedList(Iterable)
     */
    public static <E> LinkedList<E> newLinedList(Iterable<E> elements) {
        return Lists.newLinkedList(elements);
    }

    //--------------------from JDK Collections 的构造函数---------------------

    /**
     * 根据等号左边的类型，构造类型转换的CopyOnWriteArrayList, 并初始化元素.
     *
     * @param elements
     * @param <E>
     * @return
     */
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(E... elements) {
        return new CopyOnWriteArrayList<E>(elements);
    }

    /**
     * 返回一个空的结构特殊的List，节约空间.
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param <E>
     * @return
     * @see Collections#emptyList()
     */
    public static final <E> List<E> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 如果list为null，转化为一个安全的空List.
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param list
     * @param <E>
     * @return
     * @see Collections#emptyList()
     */
    public static <E> List<E> emptyListIfNull(final List<E> list) {
        return list == null ? (List<E>) Collections.EMPTY_LIST : list;
    }

    /**
     * 返回只含一个元素但结构特殊的List，节约空间.
     * 注意返回的List不可写, 写入会抛出UnsupportedOperationException.
     *
     * @param o
     * @param <E>
     * @return
     * @see Collections#singletonList(Object)
     */
    public static <E> List<E> singletonList(E o) {
        return Collections.singletonList(o);
    }

    /**
     * 返回包装后不可修改的List.
     *
     * 如果尝试写入会抛出UnsupportedOperationException.
     *
     * @see java.util.Collections#unmodifiableList(List)
     */
    /**
     * 返回包装后不可修改的List.
     * 如果尝试写入会抛出UnsupportedOperationException.
     *
     * @param list
     * @param <E>
     * @return
     * @see Collections#unmodifiableList(List)
     */
    public static <E> List<E> unmodifiableList(List<? extends E> list) {
        return Collections.unmodifiableList(list);
    }

    /**
     * 返回包装后同步的List，所有方法都会被synchronized原语同步.
     * 用于CopyOnWriteArrayList与 ArrayDequeue均不符合的场景
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> List<E> synchronizedList(List<E> list) {
        return Collections.synchronizedList(list);
    }

    //--------------------from JDK Collections 的常用函数---------------------

    /**
     * 升序排序, 采用JDK认为最优的排序算法, 使用元素自身的compareTo()方法
     *
     * @param list
     * @param <E>
     * @see Collections#sort(List)
     */
    public static <E extends Comparable<? super E>> void sort(List<E> list) {
        Collections.sort(list);
    }

    /**
     * 倒序排序, 采用JDK认为最优的排序算法,使用元素自身的compareTo()方法
     *
     * @param list
     * @param <E>
     * @see Collections#sort(List)
     */
    public static <E extends Comparable<? super E>> void sortReverse(List<E> list) {
        Collections.sort(list, Collections.reverseOrder());
    }

    /**
     * 升序排序, 采用JDK认为最优的排序算法, 使用Comparator.
     *
     * @param list
     * @param comparator
     * @param <E>
     * @see Collections#sort(List, Comparator)
     */
    public static <E extends Comparable<? super E>> void sort(List<E> list, Comparator<? super E> comparator) {
        Collections.sort(list, comparator);
    }

    /**
     * 降序排序, 采用JDK认为最优的排序算法, 使用Comparator.
     *
     * @param list
     * @param comparator
     * @param <E>
     * @see Collections#sort(List, Comparator)
     */
    public static <E extends Comparable<? super E>> void sortReverse(List<E> list, Comparator<? super E> comparator) {
        Collections.sort(list, Collections.reverseOrder(comparator));
    }

    /**
     * 二分法快速查找对象，使用Comparable对象自身的比较.
     * <p>
     * list必须已按升序排序.
     * <p>
     * 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     *
     * @param sortedList
     * @param key
     * @param <E>
     * @return
     * @see Collections#binarySearch(List, Object)
     */
    public static <E> int binarySearch(List<? extends Comparable<? super E>> sortedList, E key) {
        return Collections.binarySearch(sortedList, key);
    }

    /**
     * 二分法快速查找对象，使用Comparable对象自身的比较.
     * <p>
     * 如果不存在，返回一个负数，代表如果要插入这个对象，应该插入的位置
     *
     * @param sortedList
     * @param key
     * @param <E>
     * @return
     * @see Collections#binarySearch(List, Object, Comparator)
     */
    public static <E> int binarySearch(List<E> sortedList, E key, Comparator<? super E> comparator) {
        return Collections.binarySearch(sortedList, key, comparator);
    }

    /**
     * 随机乱序，使用默认的Random
     *
     * @param list
     * @param <E>
     * @see Collections#shuffle(List)
     */
    public static <E> void shuffle(List<E> list) {
        Collections.shuffle(list);
    }

    /**
     * 随机乱序，使用传入的Random
     *
     * @param list
     * @param <E>
     * @see Collections#shuffle(List, Random)
     */
    public static <E> void shuffle(List<E> list, Random random) {
        Collections.shuffle(list, random);
    }

    //--------------------from guava 的常用函数---------------------

    /**
     * 返回一个倒转顺序访问的List, 仅仅是一个倒序的view, 不会实际多生成一个List.
     *
     * @param list
     * @param <E>
     * @return
     * @see com.google.common.collect.Lists#reverse(List)
     */
    public static <E> List<E> reverse(List<E> list) {
        return Lists.reverse(list);
    }

    /**
     * List分页函数
     *
     * @param list
     * @param size
     * @param <E>
     * @return
     */
    public static <E> List<List<E>> partition(List<E> list, int size) {
        return Lists.partition(list, size);
    }

    /**
     * 过滤集合，通过{@link Filter#accept(Object)}返回的布尔值进行过滤
     *
     * @param list
     * @param filter 过滤器
     * @param <E>
     * @return
     */
    public static <E> void filter(List<E> list, Filter<E> filter) {
        //定义边界
        if (isEmpty(list)) {
            return;
        }

        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (!filter.accept(iterator.next())) {
                iterator.remove();
            }
        }
    }

    /**
     * 对集合元素进行去重
     *
     * @param list
     * @param <E>
     */
    public static <E> void unique(List<E> list) {
        uniqueByFilter(list, null);
    }

    /**
     * 对过滤器过滤后的集合元素进行去重
     *
     * @param list
     * @param filter
     * @param <E>
     */
    public static <E> void uniqueByFilter(List<E> list, Filter<E> filter) {
        if (isEmpty(list)) {
            return;
        }

        Iterator<E> iterator = list.iterator();
        Set<E> set = new HashSet<>((int) (list.size() / 0.75F + 1.0F));

        while (iterator.hasNext()) {
            E obj = iterator.next();
            if (filter != null && !filter.accept(obj)) {
                iterator.remove();
                continue;
            }

            /**
             * 对集合进行去重
             */
            if (set.contains(obj)) {
                iterator.remove();
                continue;
            }
            set.add(obj);
        }
    }

    //--------------------集合运算-----------------------------

    /**
     * 合并两个List
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> union(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> list = new ArrayList<>(list1.size() + list2.size());
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    /**
     * list1, list2的交集（同时在list1和list2的对象），产生新List
     * copy from Apache Common Collection4 ListUtils，但其做了不合理的去重，因此重新改为性能较低但不去重的版本
     * 与List.retainAll()相比，考虑了的List中相同元素出现的次数, 如"a"在list1出现两次，而在list2中只出现一次，则交集里会保留一个"a".
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> intersection(final List<? extends E> list1, final List<? extends E> list2) {
        List<? extends E> smaller = list1;
        List<? extends E> larger = list2;
        if (list1.size() > list2.size()) {
            smaller = list2;
            larger = list1;
        }
        /**
         * 克隆一个可修改的副本
         */
        List<E> newSmaller = new ArrayList<>(smaller);
        List<E> ret = new ArrayList<>(smaller.size());
        for (final E obj : larger) {
            if (newSmaller.contains(obj)) {
                ret.add(obj);
                newSmaller.remove(obj);
            }
        }
        return ret;
    }

    /**
     * list1, list2的差集（在list1，不在list2中的对象），产生新List.
     * 与List.removeAll()相比，会计算元素出现的次数，如"a"在list1出现两次，而在list2中只出现一次，则差集里会保留一个"a".
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> difference(final List<? extends E> list1, final List<? extends E> list2) {
        final List<E> ret = new ArrayList<>(list1.size());
        Iterator<? extends E> iterator = list2.iterator();
        while (iterator.hasNext()) {
            ret.remove(iterator.next());
        }
        return ret;
    }

    /**
     * list1, list2的补集（在list1或list2中，但不在交集中的对象，又叫反交集）产生新List.
     * <p>
     * copy from Apache Common Collection4 ListUtils，但其并集－交集时，初始大小没有对交集*2，所以做了修改
     *
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> disjoint(final List<? extends E> list1, final List<? extends E> list2) {
        List<E> intersection = intersection(list1, list2);
        List<E> towIntersection = union(intersection, intersection);
        return difference(union(list1, list2), towIntersection);
    }
}
