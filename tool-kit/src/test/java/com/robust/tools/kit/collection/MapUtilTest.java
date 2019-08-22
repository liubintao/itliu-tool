package com.robust.tools.kit.collection;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Ordering;
import com.robust.tools.kit.base.ObjectUtil;
import com.robust.tools.kit.collection.type.MoreMaps;
import org.assertj.core.api.Fail;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/25 14:34
 * @Version: 1.0
 */
public class MapUtilTest {

    @Test
    public void empty() {
        HashMap map = MapUtil.newHashMap();
        assertThat(MapUtil.isEmpty(map)).isTrue();
        assertThat(MapUtil.isEmpty(null)).isTrue();
        assertThat(MapUtil.isNotEmpty(map)).isFalse();
        assertThat(MapUtil.isNotEmpty(null)).isFalse();

        map.put("a", "abc");
        assertThat(MapUtil.isEmpty(map)).isFalse();
        assertThat(MapUtil.isNotEmpty(map)).isTrue();

        ConcurrentHashMap<String, Integer> concurrentHashMap = MapUtil.newConcurrentHashMap();
        assertThat(MapUtil.putIfAbsentReturnLast(concurrentHashMap, "a", 1)).isEqualTo(1);
        assertThat(MapUtil.putIfAbsentReturnLast(concurrentHashMap, "a", 2)).isEqualTo(1);

        assertThat(concurrentHashMap.putIfAbsent("b", 1)).isEqualTo(null);
        assertThat(concurrentHashMap.putIfAbsent("b", 2)).isEqualTo(1);

        MapUtil.createIfAbsentReturnLast(concurrentHashMap, "a", () -> 5);
        assertThat(concurrentHashMap).hasSize(2).containsEntry("a", 1);

        MapUtil.createIfAbsentReturnLast(concurrentHashMap, "c", () -> 5);
        assertThat(concurrentHashMap).hasSize(3).containsEntry("c", 5);

        MapUtil.createIfAbsentReturnLast(concurrentHashMap, "c", () -> 6);
        assertThat(concurrentHashMap).hasSize(3).containsEntry("c", 5);
    }

    @Test
    public void guavaBuild() {
        HashMap<String, Integer> map1 = MapUtil.newHashMapWithCapacity(3, 0.75F);
        map1 = MapUtil.newHashMapWithCapacity(3, 0.5f);

        HashMap<String, Integer> map2 = MapUtil.newHashMap("a", 1);
        assertThat(map2).hasSize(1).containsEntry("a", 1);

        TreeMap<String, Integer> map3 = MapUtil.newTreeMap();
        TreeMap<String, Integer> map4 = MapUtil.newTreeMap(Ordering.natural());

        ConcurrentSkipListMap<String, Integer> skipListMap = MapUtil.newConcurrentSkipListMap();
        EnumMap enumMap = MapUtil.newEnumMap(Cert.class);
    }

    public enum Cert {
        A, B, C;
    }

    @Test
    public void jdkBuild() {
        HashMap<String, Integer> map1 = MapUtil.newHashMap();

        Map<String, Integer> map2 = MapUtil.emptyMap();
        assertThat(map2).hasSize(0).isNotNull();

        Map<String, Integer> map3 = MapUtil.emptyMapIfNull(null);
        assertThat(map3).hasSize(0);

        Map<String, Integer> map4 = MapUtil.emptyMapIfNull(map1);
        assertThat(map4).hasSize(0).isSameAs(map1);

        Map<String, Integer> map5 = MapUtil.singletonMap("a", 1);
        assertThat(map5).hasSize(1).containsEntry("a", 1);

        try {
            map5.put("b", 2);
            Fail.fail("should fail before");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }

        Map<String, Integer> map6 = MapUtil.unmodifiableMap(map5);
        try {
            map6.put("c", 2);
            Fail.fail("should fail before");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Test
    public void weakMap() throws InterruptedException {
        ConcurrentMap<MyBean, MyBean> weakKeyMap = MoreMaps.createWeakKeyConcurrentMap(10, 1);
        initExpireAllMap(weakKeyMap);
        System.out.println("beforeGC:"+ weakKeyMap.toString());
        System.gc();
        System.out.println("afterGC:"+weakKeyMap.toString());
        assertThat(weakKeyMap.get(new MyBean("A"))).isNull();
        assertThat(weakKeyMap).hasSize(1); // key仍然在
        weakKeyMap.put(new MyBean("C"),new MyBean("D"));
        assertThat(weakKeyMap).hasSize(1);

        ConcurrentMap<MyBean, MyBean> weakKeyMap2 = MoreMaps.createWeakKeyConcurrentMap(10, 1);
        MyBean value = new MyBean("B");
        initExpireKeyMap(weakKeyMap2, value);
        System.out.println("beforeGC:"+ weakKeyMap2.toString());
        System.gc();
        System.out.println("afterGC:"+weakKeyMap2.toString());
        assertThat(weakKeyMap2.get(new MyBean("A"))).isNull();

        ConcurrentMap<MyBean, MyBean> weakKeyMap3 = MoreMaps.createWeakKeyConcurrentMap(10, 1);
        MyBean key = new MyBean("A");
        initExpireValueMap(weakKeyMap3, key);
        System.out.println("beforeGC:"+ weakKeyMap3.toString());
        System.gc();
        System.out.println("afterGC:"+weakKeyMap3.toString());
        assertThat(weakKeyMap3.get(key)).isEqualTo(new MyBean("B"));

        // weak value
        ConcurrentMap<MyBean, MyBean> weakValueMap = MoreMaps.createWeakValueConcurrentMap(10, 1);
        initExpireAllMap(weakValueMap);
        System.out.println("beforeGC:"+ weakValueMap.toString());
        System.gc();
        System.out.println("afterGC:"+weakValueMap.toString());
        assertThat(weakValueMap.get(new MyBean("A"))).isNull();

        ConcurrentMap<MyBean, MyBean> weakValueMap2 = MoreMaps.createWeakValueConcurrentMap(10, 1);
        MyBean value2 = new MyBean("B");
        initExpireKeyMap(weakValueMap2, value2);
        System.out.println("beforeGC:"+ weakValueMap2.toString());
        System.gc();
        System.out.println("afterGC:"+weakValueMap2.toString());
        assertThat(weakValueMap2.get(new MyBean("A"))).isEqualTo(new MyBean("B"));

        ConcurrentMap<MyBean, MyBean> weakValueMap3 = MoreMaps.createWeakValueConcurrentMap(10, 1);
        MyBean key3 = new MyBean("A");
        initExpireValueMap(weakValueMap3, key3);
        System.out.println("beforeGC:"+ weakValueMap3.toString());
        System.gc();
        System.out.println("afterGC:"+weakValueMap3.toString());
        assertThat(weakValueMap3.get(new MyBean("A"))).isNull();


        ConcurrentMap<MyBean, Integer> weakValueMap4 = new MapMaker()
                .weakKeys() // 指定Map保存的Key为WeakReference机制
                .makeMap();

        MyBean key1 = new MyBean("A");
        weakValueMap4.put(key1, 2); // 加入元素
        key1 = null; // key变成了WeakReference
        System.out.println("beforeGC:"+ ObjectUtil.toPrettyString(weakValueMap4));
        List<Byte[]> list = new ArrayList<>();
        for(int i=0;i<1000;i++){
            list.add(new Byte[1024]);
        }
        System.out.println("afterGC:"+ObjectUtil.toPrettyString(weakValueMap4));
        assertThat(weakValueMap4).hasSize(1); // map空了，因为WeakReference被回收
    }

    // 抽出子函数，使得Key/Value的生命周期过期
    private void initExpireAllMap(ConcurrentMap<MyBean, MyBean> weakKeyMap) {
        MyBean key = new MyBean("A");
        MyBean value = new MyBean("B");
        weakKeyMap.put(key, value);
        assertThat(weakKeyMap.get(key)).isEqualTo(value);
    }

    // 抽出子函数，使得key过期，value不过期
    private void initExpireKeyMap(ConcurrentMap<MyBean, MyBean> weakKeyMap, MyBean value) {
        MyBean key = new MyBean("A");
        weakKeyMap.put(key, value);
        assertThat(weakKeyMap.get(key)).isEqualTo(value);
    }

    // 抽出子函数，使得key不过期，value过期
    private void initExpireValueMap(ConcurrentMap<MyBean, MyBean> weakKeyMap, MyBean key) {
        MyBean value = new MyBean("B");
        weakKeyMap.put(key, value);
        assertThat(weakKeyMap.get(key)).isEqualTo(value);
    }

    public static class MyBean {
        String name;

        public MyBean(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            MyBean other = (MyBean) obj;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            return true;
        }

    }
}