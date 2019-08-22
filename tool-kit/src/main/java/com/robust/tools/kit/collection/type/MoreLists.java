package com.robust.tools.kit.collection.type;

import java.util.Comparator;

/**
 * @Description: 增强版(特殊)的List工具类
 * @Author: robust
 * @CreateDate: 2019/7/29 17:33
 * @Version: 1.0
 */
public class MoreLists {

    /**
     * 排序的ArrayList
     * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
     *
     * @param <E>
     * @return
     */
    public static <E extends Comparable> SortedArrayList<E> createSortedArrayList() {
        return new SortedArrayList<>();
    }

    /**
     * 排序的ArrayList.
     * <p>
     * from Jodd的新类型，插入时排序，但指定插入index的方法如add(index,element)不支持
     */
    public static <T> SortedArrayList<T> createSortedArrayList(Comparator<? super T> c) {
        return new SortedArrayList<T>(c);
    }
}
