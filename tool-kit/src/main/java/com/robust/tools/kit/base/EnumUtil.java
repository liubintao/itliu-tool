package com.robust.tools.kit.base;

import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Objects;

/**
 * @Description: 枚举工具类
 * 1、将多个枚举值按bit与long的转换
 * 2、与String的转换
 * 3、根据枚举自定义的值获取对应的枚举
 * @Author: robust
 * @CreateDate: 2019/7/22 10:59
 * @Version: 1.0
 */
public class EnumUtil {

    /**
     * 将若干个枚举值转换为long(按bits 1,2,4,8...的方式叠加)，用于使用long保存多个选项的情况.
     *
     * @param enumClz
     * @param iterable
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> long generateBits(final Class<E> enumClz, final Iterable<? extends E> iterable) {
        return EnumUtils.generateBitVector(enumClz, iterable);
    }

    /**
     * 将若干个枚举值转换为long(按bits 1,2,4,8...的方式叠加)，用于使用long保存多个选项的情况.
     */
    public static <E extends Enum<E>> long generateBits(final Class<E> enumClz, final E... values) {
        return EnumUtils.generateBitVector(enumClz, values);
    }

    /**
     * long重新解析为若干个枚举值，用于使用long保存多个选项的情况.
     */
    public static <E extends Enum<E>> EnumSet<E> processBits(final Class<E> enumClz, final long value) {
        return EnumUtils.processBitVector(enumClz, value);
    }

    /**
     * Enum转换为String
     */
    public static String toString(Enum e) {
        return e.name();
    }

    /**
     * String转换为Enum
     */
    public static <E extends Enum<E>> E from(Class<E> enumClz, String value) {
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumClz, value);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 根据枚举中自定义的Filed获取对应的枚举
     *
     * @param enumClz    枚举类
     * @param code       枚举唯一值
     * @param methodName 可通过枚举唯一值获取对应枚举的方法
     * @param <E>
     * @return
     */
    public static <E extends Enum<E>> E fromByCode(Class<E> enumClz, String code, String methodName) {
        if (code == null) {
            return null;
        }
        Method method;
        try {
            method = enumClz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
        E[] enumArray = enumClz.getEnumConstants();
        String ret;
        for (int i = 0, j = enumArray.length; i < j; i++) {
            E e = enumArray[i];
            try {
                ret = (String) method.invoke(e);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                return null;
            }
            if (Objects.deepEquals(code, ret)) {
                return e;
            }
        }
        return null;
    }
}
