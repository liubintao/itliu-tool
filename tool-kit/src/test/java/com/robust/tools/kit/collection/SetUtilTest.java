package com.robust.tools.kit.collection;

import com.google.common.collect.Ordering;
import com.robust.tools.kit.collection.type.ConcurrentHashSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/29 15:37
 * @Version: 1.0
 */
public class SetUtilTest {

    @Test
    public void guavaBuild() {
        HashSet set1 = SetUtil.newHashSet();
        HashSet set2 = SetUtil.newHashSet("1","2","3");
        HashSet set3 = SetUtil.newHashSet(ListUtil.newArrayList("1","2","3"));
        HashSet set4 = SetUtil.newHashWithCapacity(5);

        assertThat(set1).isNotNull().isEmpty();
        assertThat(set2).hasSize(3).contains("1");
        assertThat(set3).hasSize(3).contains("1","2");
        assertThat(set4).hasSize(0).isEmpty();

        TreeSet set5 = SetUtil.newTreeSet();
        TreeSet set6 = SetUtil.newTreeSet(Ordering.natural());
        TreeSet set7 = SetUtil.newTreeSet(ListUtil.newArrayList("1","2","3"));

        assertThat(set5).hasSize(0).isNotNull();
        assertThat(set6).hasSize(0).isNotNull();
        assertThat(set7).hasSize(3).isNotNull().contains("2","3");

        ConcurrentHashSet hashSet = SetUtil.newConcurrentHashSet();
        assertThat(hashSet).hasSize(0);

        hashSet.add("1");
        hashSet.add("2");
        assertThat(hashSet).hasSize(2);
    }

    @Test
    public void jdkBuild() {
        Set set1 = SetUtil.emptySet();
        assertThat(set1).hasSize(0).isNotNull().isEmpty();

        try {
            set1.add(1);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UnsupportedOperationException.class);
        }

        Set set2 = SetUtil.emptySetIfNull(null);
        assertThat(set2).hasSize(0).isNotNull().isEmpty();

        Set set3 = SetUtil.singletonSet(1);
        assertThat(set3).hasSize(1).isNotNull();

        Set set4 = SetUtil.unmodifiableSet(set3);
        assertThat(set4).hasSize(1).isNotNull().isEqualTo(set3);

        Set set5 = SetUtil.newSetFromMap(MapUtil.newConcurrentHashMap());
    }

    @Test
    public void collectionCalculate() {
        HashSet<String> set1 = SetUtil.newHashSet("1", "2", "3", "6");
        HashSet<String> set2 = SetUtil.newHashSet("4", "5", "6", "7");

        Set<String> set3 = SetUtil.unionView(set1, set2);
        assertThat(set3).hasSize(7).contains("1", "2", "3", "4", "5", "6", "7");

        Set<String> set4 = SetUtil.intersectionView(set1, set2);
        assertThat(set4).hasSize(1).contains("6");

        Set<String> set5 = SetUtil.differenceView(set1, set2);
        assertThat(set5).hasSize(3).contains("1", "2", "3");

        Set<String> set6 = SetUtil.disjointView(set1, set2);
        assertThat(set6).hasSize(6).contains("1", "2", "3", "4", "5", "7");

        try {
            set6.add("a");
            fail("should fail before");
        } catch (Throwable t) {
            assertThat(t).isInstanceOf(UnsupportedOperationException.class);
        }
    }
}