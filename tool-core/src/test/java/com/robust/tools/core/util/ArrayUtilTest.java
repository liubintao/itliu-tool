package com.robust.tools.core.util;

import com.robust.tools.kit.collection.ArrayUtil;
import com.robust.tools.kit.lang.Editor;
import com.robust.tools.kit.lang.Filter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/16 11:22
 * @Version: 1.0
 */
public class ArrayUtilTest {

    @Test
    public void isEmptyTest() {
        Object[] arr = null;
        Assert.assertTrue(ArrayUtil.isEmpty(arr));
        arr = new Object[]{};
        Assert.assertTrue(ArrayUtil.isEmpty(arr));
        int[] iArray = {};
        Assert.assertTrue(ArrayUtil.isEmpty(iArray));
    }

    @Test
    public void isNotEmptyTest() {
        Object[] arr = new Object[1];
        Assert.assertTrue(ArrayUtil.isNotEmpty(arr));
        arr = new Object[]{new Object()};
        Assert.assertTrue(ArrayUtil.isNotEmpty(arr));
        int[] iArray = {1};
        Assert.assertTrue(ArrayUtil.isNotEmpty(iArray));
    }

    @Test
    public void hasNullTest() {
        Object[] arr = new Object[1];
        Assert.assertTrue(ArrayUtil.hasNull(arr));
        arr[0] = new Object();
        Assert.assertFalse(ArrayUtil.hasNull(arr));
    }

    @Test
    public void firstNonNullTest() {
        Object[] arr = new Object[]{null, null, null};
        Assert.assertNull(ArrayUtil.firstNonNull(arr));
        arr[2] = new Object();
        Assert.assertNotNull(ArrayUtil.firstNonNull(arr));
    }

    @Test
    public void newArrayTest() {
        Person[] persons = ArrayUtil.newArray(Person.class, 2);
        System.out.println(Arrays.toString(persons));
        Assert.assertNotNull(persons);
    }

    @Test
    public void filterTest() {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6};
        Integer[] array = ArrayUtil.filter(ArrayUtil.wrap(arr), new Editor<Integer>() {
            @Override
            public Integer edit(Integer integer) {
                if (integer.intValue() % 2 == 0) {
                    return integer;
                }
                return null;
            }
        });
        System.out.println(Arrays.toString(array));
        Assert.assertSame(3, array.length);

        Integer[] array1 = ArrayUtil.filter(ArrayUtil.wrap(arr), new Filter<Integer>() {
            @Override
            public boolean accept(Integer integer) {
                return false;
            }
        });

        Assert.assertEquals(0, array1.length);
    }

    @Test
    public void getTest() {
        int[] arr = new int[]{1,2,3};
        Assert.assertEquals(1, (int)ArrayUtil.get(arr, 0));
        Assert.assertEquals(2, (int)ArrayUtil.get(arr, 1));
        Assert.assertEquals(3, (int)ArrayUtil.get(arr, 2));
        Assert.assertEquals(3, (int)ArrayUtil.get(arr, -1));
        Assert.assertEquals(2, (int)ArrayUtil.get(arr, -2));
        Assert.assertEquals(1, (int)ArrayUtil.get(arr, -3));
    }

    @Test
    public void indexOfTest() {
        int[] arr = new int[] {1,2,3};
        Assert.assertEquals(0, ArrayUtil.indexOf(ArrayUtil.wrap(arr), 1));
        Assert.assertEquals(1, ArrayUtil.indexOf(ArrayUtil.wrap(arr), 2));
        Assert.assertEquals(2, ArrayUtil.indexOf(ArrayUtil.wrap(arr), 3));

        Person[] people = new Person[3];
        Person p1 = new Person("zhangsan", 1);
        Person p2 = new Person("lisi", 2);
        Person p3 = new Person("wangwu", 3);
        people[0] = p1;
        people[1] = p2;
        people[2] = p3;
        Assert.assertEquals(p1, ArrayUtil.get(people, 0));
        Assert.assertEquals(p2, ArrayUtil.get(people, 1));
        Assert.assertEquals(p3, ArrayUtil.get(people, 2));
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    static class Person {
        private String name;
        private int age;
    }
}