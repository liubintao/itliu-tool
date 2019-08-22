package com.robust.tools.kit.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.robust.tools.kit.base.annotation.NotNull;
import com.robust.tools.kit.base.annotation.Nullable;
import org.apache.commons.lang3.concurrent.ConcurrentInitializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Description: Map工具类
 * @Author: robust
 * @CreateDate: 2019/7/18 9:50
 * @Version: 1.0
 */
public class MapUtil {

    /**
     * Map是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }

    /**
     * ConcurrentHashMap的putIfAbsent返回之前的Value，此函数封装返回最终存储的Value
     *
     * @param map
     * @param key
     * @param val
     * @param <K>
     * @param <V>
     * @return
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#putIfAbsent(ConcurrentMap, Object, Object)
     */
    public static <K, V> V putIfAbsentReturnLast(@NotNull final ConcurrentHashMap<K, V> map, final K key, final V val) {
        final V result = map.putIfAbsent(key, val);
        return result != null ? result : val;
    }

    /**
     * 如果Key不存在则创建，返回最后存储在Map中的Value.
     * 如果创建Value对象有一定成本, 直接使用PutIfAbsent可能重复浪费，则使用此类，传入一个被回调的ValueCreator，Lazy创建对象。
     *
     * @param map
     * @param key
     * @param creator
     * @param <K>
     * @param <V>
     * @return
     * @see org.apache.commons.lang3.concurrent.ConcurrentUtils#createIfAbsent(ConcurrentMap, Object, ConcurrentInitializer)
     */
    public static <K, V> V createIfAbsentReturnLast(@NotNull final ConcurrentHashMap<K, V> map, final K key,
                                                    @NotNull final ValueCreator<V> creator) {
        final V val = map.get(key);
        if (val == null) {
            return putIfAbsentReturnLast(map, key, creator.get());
        }
        return val;
    }

    /**
     * 延迟创建Value值的回调类
     *
     * @param <T>
     */
    public interface ValueCreator<T> {
        T get();
    }

    /**
     * 根据等号左边的类型，构造类型正确的HashMap.
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /*--------------from guava的构造函数--------------------------*/

    /**
     * 根据等号左边的类型，构造类型正确的HashMap
     * 注意HashMap中有0.75的加载因子的影响，需要进行运算后才能正确初始化HashMap的大小.
     * 加载因子也是HashMap中减少Hash冲突的一环，如果读写频繁，总记录数不多的Map，可以比默认值0.75进一步降低，建议0.5
     *
     * @param expectedSize
     * @param loadFactor
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMapWithCapacity(int expectedSize, float loadFactor) {
        int finalSize = (int) (expectedSize / loadFactor + 1.0F);
        return new HashMap<>(finalSize, loadFactor);
    }

    /**
     * 根据等号左边的类型，构造类型正确的HashMap，并初始化第一个元素
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> HashMap<K, V> newHashMap(final K key, final V value) {
        HashMap<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeMap.
     *
     * @param <K>
     * @param <V>
     * @return
     * @see Maps#newTreeMap()
     */
    public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的TreeMap.
     *
     * @param comparator
     * @param <C>
     * @param <K>
     * @param <V>
     * @return
     * @see Maps#newTreeMap(Comparator)
     */
    public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
        return Maps.newTreeMap(comparator);
    }

    /**
     * 相比HashMap，当key是枚举类时, 性能与空间占用俱佳.
     *
     * @param type
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(@NotNull Class<K> type) {
        return new EnumMap<K, V>(Preconditions.checkNotNull(type));
    }

    /**
     * 根据等号左边的类型，构造类型正确的ConcurrentHashMap.
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 根据等号左边的类型，构造类型正确的ConcurrentSkipListMap.
     *
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> ConcurrentSkipListMap<K, V> newConcurrentSkipListMap() {
        return new ConcurrentSkipListMap<>();
    }

    /*---------------from JDK Collections的常用构造函数-------------------------*/

    /**
     * 返回一个空结构的特殊Map，节约空间.
     * 注意返回的Map不可写，写入会抛出UnsupportedOperationException.
     *
     * @param <K>
     * @param <V>
     * @return
     * @see Collections#emptyMap()
     */
    public static final <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    /**
     * 如果Map为null，转化为安全的空Map
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> emptyMapIfNull(final Map<K, V> map) {
        return map == null ? emptyMap() : map;
    }

    /**
     * 返回只有一个元素的特殊结构Map，节约空间.
     *
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     * @see Collections#singletonMap(Object, Object)
     */
    public static <K, V> Map<K, V> singletonMap(final K key, final V value) {
        return Collections.singletonMap(key, value);
    }

    /**
     * 返回包装后不可修改的Map，如果尝试修改会抛出UnsupportedOperationException.
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     * @see Collections#unmodifiableMap(Map)
     */
    public static <K, V> Map<K, V> unmodifiableMap(final Map<? extends K, ? extends V> map) {
        return Collections.unmodifiableMap(map);
    }

    /**
     * 返回包装后不可修改的有序Map，如果尝试修改会抛出UnsupportedOperationException.
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     * @see Collections#unmodifiableSortedMap(SortedMap)
     */
    public static <K, V> SortedMap<K, V> unmodifiableSortedMap(final SortedMap<K, ? extends V> map) {
        return Collections.unmodifiableSortedMap(map);
    }

    /*------------------------对两个Map进行diff的操作----------------------------------*/

    /**
     * 对两个Map进行比较，返回MapDifference，然后各种妙用.
     * 包含key的差集，key的交集，以及key相同但value不同的元素.
     *
     * @param left
     * @param right
     * @param <K>
     * @param <V>
     * @return
     * @see MapDifference
     */
    public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left,
                                                        Map<? extends K, ? extends V> right) {
        return Maps.difference(left, right);
    }

    /*-------------------按值排序取TopN的操作------------------------*/

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap，多用于Value是Counter的情况.
     *
     * @param map
     * @param reverse
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable> Map<K, V> sortByValue(Map<K, V> map, final boolean reverse) {
        return sortByValueInternal(map, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<K, V>());
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap
     *
     * @param map
     * @param comparator
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> sortByValue(Map<K, V> map, final Comparator<? super V> comparator) {
        return sortByValueInternal(map, new EntryValueComparator<>(comparator));
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap
     *
     * @param map
     * @param comparator
     * @param <K>
     * @param <V>
     * @return
     */
    private static <K, V> Map<K, V> sortByValueInternal(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        Map.Entry<K, V>[] entryArray = entrySet.toArray(new Map.Entry[0]);

        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<>();
        for (int i = 0; i < entryArray.length; i++) {
            Map.Entry<K, V> entry = entryArray[i];
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap，最多只返回N条，多用于Value是Counter的情况.
     *
     * @param map
     * @param reverse 按Value的倒序 or 正序
     * @param n
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable> Map<K, V> topNByValue(Map<K, V> map, final boolean reverse, int n) {
        return topNByValueInternal(map, n, reverse ? Ordering.from(new ComparableEntryValueComparator<K, V>()).reverse()
                : new ComparableEntryValueComparator<>());
    }

    /**
     * 对一个Map按Value进行排序，返回排序LinkedHashMap，最多只返回N条，多用于Value是Counter的情况.
     *
     * @param map
     * @param comparator
     * @param n
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> topNByValue(Map<K, V> map, final Comparator<? super V> comparator, final int n) {
        return topNByValueInternal(map, n, new EntryValueComparator<>(comparator));
    }

    private static <K, V> Map<K, V> topNByValueInternal(Map<K, V> map, final int n, Comparator<Map.Entry<K, V>> comparator) {
        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        Map.Entry<K, V>[] entryArray = entrySet.toArray(new Map.Entry[0]);
        Arrays.sort(entryArray, comparator);

        Map<K, V> result = new LinkedHashMap<>();
        int size = Math.min(n, entryArray.length);

        for (int i = 0; i < size; i++) {
            Map.Entry<K, V> entry = entryArray[i];
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static final class ComparableEntryValueComparator<K, V extends Comparable> implements Comparator<Map.Entry<K, V>> {

        @Override
        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    }

    private static final class EntryValueComparator<K, V> implements Comparator<Map.Entry<K, V>> {
        private final Comparator<? super V> comparator;

        private EntryValueComparator(Comparator<? super V> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return comparator.compare(o1.getValue(), o2.getValue());
        }
    }
}
