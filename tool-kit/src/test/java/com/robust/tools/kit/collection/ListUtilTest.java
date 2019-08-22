package com.robust.tools.kit.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/22 17:19
 * @Version: 1.0
 */
public class ListUtilTest {

    @Test
    public void filter() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        ListUtil.filter(list, (i) -> i % 2 == 0);
        System.out.println(list);
    }

    @Test
    public void uniqueByFilterTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(5);
        ListUtil.uniqueByFilter(list, (t) -> t % 2 == 0);
        System.out.println(list);
    }

    @Test
    public void uniqueTest() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(5);
        ListUtil.unique(list);
        System.out.println(list);
    }

    @Test
    public void name() {
    }
}