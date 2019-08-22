package com.robust.tools.kit.base;

import com.robust.tools.kit.base.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Description: 1、Object打印优化，主要是数组打印
 * 2、多个对象的Hashcode关联
 * @Author: robust
 * @CreateDate: 2019/7/23 7:31
 * @Version: 1.0
 */
public class ObjectUtil {
    private static final String NULL = "null";

    /**
     * JDK1.7引入Null安全的equals方法
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(@Nullable Object a, @Nullable Object b) {
        return Objects.equals(a, b);
    }

    /**
     * 多个对象的hashcode串联，组成新的hashcode
     *
     * @param objects
     * @return
     */
    public static int hashcode(Objects... objects) {
        return Arrays.hashCode(objects);
    }

    /**
     * 对象的toString(), 处理了对象为数组的情况，JDK的默认toString()只打数组的地址如 "[Ljava.lang.Integer;@490d6c15.
     */
    public static String toPrettyString(Object value) {
        if(value == null) {
            return NULL;
        }
        
        Class<?> clz = value.getClass();
        if(clz.isArray()) {
            Class<?> componentType = clz.getComponentType();
            if(componentType.isPrimitive()) {
                return primitiveArray2String(value, componentType);
            } else {
                return objectArray2String(value);
            }
        } else if(value instanceof Iterable) {
            // 因为Collection的处理也是默认调用元素的toString(),
            // 为了处理元素是数组的情况，同样需要重载
            return collection2String(value);
        }
        return value.toString();
    }

    private static String objectArray2String(Object value) {
        Object[] array = (Object[]) value;
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        int i = 0;
        for(Object o : array) {
            if(i > 0) {
                sb.append(", ");
            }
            sb.append(toPrettyString(o));
            i++;
        }
        sb.append(']');
        return sb.toString();
    }

    private static String primitiveArray2String(Object value, Class<?> componentType) {
        StringBuilder sb = new StringBuilder();

        if (componentType == int.class) {
            sb.append(Arrays.toString((int[]) value));
        } else if (componentType == long.class) {
            sb.append(Arrays.toString((long[]) value));
        } else if (componentType == double.class) {
            sb.append(Arrays.toString((double[]) value));
        } else if (componentType == float.class) {
            sb.append(Arrays.toString((float[]) value));
        } else if (componentType == boolean.class) {
            sb.append(Arrays.toString((boolean[]) value));
        } else if (componentType == short.class) {
            sb.append(Arrays.toString((short[]) value));
        } else if (componentType == byte.class) {
            sb.append(Arrays.toString((byte[]) value));
        } else if (componentType == char.class) {
            sb.append(Arrays.toString((char[]) value));
        } else {
            throw new IllegalArgumentException("unSupport array type");
        }

        return sb.toString();
    }

    public static String collection2String(Object value) {
        Iterable iterable = (Iterable) value;
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        int i = 0;
        for(Object o: iterable) {
            if(i > 0) {
                sb.append(',');
            }
            sb.append(toPrettyString(o));
            i++;
        }
        sb.append('}');
        return sb.toString();
    }
}
