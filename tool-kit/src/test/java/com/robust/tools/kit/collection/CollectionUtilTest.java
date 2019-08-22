package com.robust.tools.kit.collection;

import com.google.common.collect.Ordering;
import com.robust.tools.kit.base.type.Pair;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/24 16:01
 * @Version: 1.0
 */
public class CollectionUtilTest {

    @Test
    public void test() {
        List list = ListUtil.newArrayList();
        List list1 = ListUtil.newArrayList("a", "b");
        List list2 = ListUtil.newArrayList("a", "b");

        assertThat(list.size()).isEqualTo(0);
        assertThat(CollectionUtil.isEmpty(list1)).isFalse();
        assertThat(CollectionUtil.isNotEmpty(list1)).isTrue();
        assertThat(CollectionUtil.getFirst(list1)).isEqualTo("a");
        assertThat(CollectionUtil.getLast(list1)).isEqualTo("b");
        assertThat(CollectionUtil.elementEquals(list1, list2)).isTrue();
    }

    @Test
    public void minAndMax() {
        List<Integer> list = ListUtil.newArrayList(1, 200, 15, 49, 3000);

        assertThat(CollectionUtil.min(list)).isEqualTo(1);
        assertThat(CollectionUtil.min(list, Ordering.natural())).isEqualTo(1);

        assertThat(CollectionUtil.max(list)).isEqualTo(3000);
        assertThat(CollectionUtil.max(list, Ordering.natural())).isEqualTo(3000);

        assertThat(CollectionUtil.minAndMax(list)).isEqualTo(Pair.of(1, 3000));
        assertThat(CollectionUtil.minAndMax(list, Ordering.natural())).isEqualTo(Pair.of(1, 3000));

        assertThat(CollectionUtil.topN(list, 2)).containsExactly(3000, 200);
        assertThat(CollectionUtil.topN(list, 2, Ordering.natural())).containsExactly(3000, 200);

        assertThat(CollectionUtil.bottomN(list, 2)).containsExactly(1, 15);
        assertThat(CollectionUtil.bottomN(list, 2, Ordering.natural())).containsExactly(1, 15);
    }
}