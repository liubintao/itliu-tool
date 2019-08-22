package com.robust.tools.kit.collection;

import com.robust.tools.kit.base.type.UnCheckedException;
import com.robust.tools.kit.lang.Editor;
import com.robust.tools.kit.lang.Filter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 数组工具类
 * @Author: robust
 * @CreateDate: 2019/7/16 10:58
 * @Version: 1.0
 */
public class ArrayUtil {

    /**
     * 数组中元素未找到的下标，值为-1
     */
    private static final int INDEX_NOT_FOUND = -1;

    /**
     * 对象是否为数组对象
     *
     * @param obj 对象
     * @return 是否为数组对象，如果为{@code null} 返回false
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            // throw new NullPointerException("Object check for isArray is null");
            return false;
        }
        return obj.getClass().isArray();
    }

    /**
     * 判断数组是否为空
     *
     * @param array 数组
     * @param <T>   数组中元素类型
     * @return 是否为空
     */
    public static <T> boolean isEmpty(final T... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final int... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final short... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final byte... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final long... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final char... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final float... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final double... array) {
        return array == null || array.length == 0;
    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static boolean isEmpty(final boolean... array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(final T... array) {
        return !isEmpty(array);
    }

    /**
     * 返回包装后的数组
     *
     * @param array 原基础类型数组
     * @return
     */
    public static Integer[] wrap(final int... array) {
        if (null == array) {
            return null;
        }
        final int length = array.length;
        if (0 == length) {
            return new Integer[0];
        }

        final Integer[] integers = new Integer[length];
        for (int i = 0; i < length; i++) {
            integers[i] = Integer.valueOf(array[i]);
        }
        return integers;
    }

    /**
     * 返回原始类型后的数组
     *
     * @param values 原类型数组
     * @return
     */
    public static int[] unWrap(final Integer... values) {
        if (null == values) {
            return null;
        }
        final int length = values.length;
        if (0 == length) {
            return new int[0];
        }

        final int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = values[i].intValue();
        }
        return array;
    }

    /**
     * 返回包装后的数组
     *
     * @param values 原基础类型数组
     * @return
     */
    public static Long[] wrap(final long... values) {
        if (null == values) {
            return null;
        }
        final int length = values.length;
        if (0 == length) {
            return new Long[0];
        }

        final Long[] array = new Long[length];
        for (int i = 0; i < length; i++) {
            array[i] = Long.valueOf(values[i]);
        }
        return array;
    }

    /**
     * 判断数组是否包含空元素
     *
     * @param array 数组
     * @param <T>   数组中元素类型
     * @return 是否包含空元素
     */
    public static <T> boolean hasNull(final T... array) {
        if (isNotEmpty(array)) {
            for (T t : array) {
                if (t == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 返回数组中第一个非空的元素
     *
     * @param array 数组
     * @param <T>   数组中元素类型
     * @return
     */
    public static <T> T firstNonNull(final T... array) {
        if (isNotEmpty(array)) {
            for (T t : array) {
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 新建空数组
     *
     * @param componentType
     * @param length
     * @param <T>
     * @return
     */
    public static <T> T[] newArray(Class<?> componentType, int length) {
        return (T[]) Array.newInstance(componentType, length);
    }

    /**
     * 根据下标获取数组中的元素，下标可为负数，如-1代表倒叙第一个元素
     *
     * @param array 数组
     * @param index 下标
     * @param <T>   元素类型
     * @return
     */
    public static <T> T get(Object array, int index) {
        if (null == array) {
            return null;
        }
        if (index < 0) {
            index += Array.getLength(array);
        }
        try {
            return (T) Array.get(array, index);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * 返回目标值在数组中的下标值，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param array 数组
     * @param value 目标值
     * @param <T>   数组类型
     * @return 目标值在数组中的下标索引，未找到返回{@link #INDEX_NOT_FOUND}
     */
    public static <T> int indexOf(T[] array, Object value) {
        if (null != array) {
            for (int i = 0; i < array.length; i++) {
                if (array[i] == value || (value != null && value.equals(array[i]))) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 返回数组中指定元素所在最后的位置，未找到返回{@link #INDEX_NOT_FOUND}
     *
     * @param <T>   数组类型
     * @param array 数组
     * @param value 被检查的元素
     * @return 数组中指定元素所在位置，未找到返回{@link #INDEX_NOT_FOUND}
     */
    public static <T> int lastIndexOf(T[] array, Object value) {
        if (null != array) {
            for (int i = array.length - 1; i >= 0; i--) {
                if (array[i] == value || (value != null && value.equals(array[i]))) {
                    return i;
                }
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * 过滤<br>
     * 过滤过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
     *
     * <pre>
     * 1、过滤出需要的对象，如果返回null表示这个元素对象抛弃
     * 2、修改元素对象，返回集合中为修改后的对象
     * </pre>
     *
     * @param <T>    数组元素类型
     * @param array  数组
     * @param editor 编辑器接口
     * @return 过滤后的数组
     */
    public static <T> T[] filter(T[] array, Editor<T> editor) {
        if (null == editor) {
            return array;
        }
        final List<T> list = new ArrayList<>(array.length);
        T modified;
        for (T t : array) {
            modified = editor.edit(t);
            if (null != modified) {
                list.add(modified);
            }
        }
        return list.toArray(Arrays.copyOf(array, list.size()));
    }

    /**
     * 根据过滤规则返回过滤后的数组
     *
     * @param array  原数组
     * @param filter 过滤规则
     * @param <T>    数组类型
     * @return
     */
    public static <T> T[] filter(T[] array, Filter<T> filter) {
        if (null == filter) {
            return array;
        }
        final List<T> list = new ArrayList<>(array.length);
        for (T t : array) {
            if (filter.accept(t)) {
                list.add(t);
            }
        }
        final T[] result = newArray(array.getClass().getComponentType(), list.size());
        return list.toArray(result);
    }

    /**
     * 数组或集合转String
     *
     * @param obj 集合或数组对象
     * @return 数组字符串，与集合转字符串格式相同
     */
    public static String toString(Object obj) {
        if (null == obj) {
            return null;
        }
        if (ArrayUtil.isArray(obj)) {
            try {
                return Arrays.deepToString((Object[]) obj);
            } catch (Exception e) {
                final String className = obj.getClass().getComponentType().getName();
                switch (className) {
                    case "long":
                        return Arrays.toString((long[]) obj);
                    case "int":
                        return Arrays.toString((int[]) obj);
                    case "short":
                        return Arrays.toString((short[]) obj);
                    case "char":
                        return Arrays.toString((char[]) obj);
                    case "byte":
                        return Arrays.toString((byte[]) obj);
                    case "boolean":
                        return Arrays.toString((boolean[]) obj);
                    case "float":
                        return Arrays.toString((float[]) obj);
                    case "double":
                        return Arrays.toString((double[]) obj);
                    default:
                        throw new UnCheckedException(e);
                }
            }
        }
        return obj.toString();
    }

    /**
     * 合并两个数组
     *
     * @param array1
     * @param array2
     * @param <T>
     * @return
     */
    public static <T> T[] addAll(T[] array1, T... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }

        final Class<?> type1 = array1.getClass().getComponentType();
        final T[] joinedArray = newArray(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException e) {
            /**
             * 检查是否由于类型不兼容引发的问题
             *
             * 默认认为两个数组的类型一样，出现问题时再进行检查，这样会提升效率
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Can't store " + type2.getName() + " in an array of "
                        + type1.getName());
            }
            /**
             * 如果不是类型不兼容，则重新抛出
             */
            throw e;
        }

        return joinedArray;
    }

    /**
     * 返回浅克隆数组
     *
     * @param array
     * @param <T>
     * @return
     */
    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
