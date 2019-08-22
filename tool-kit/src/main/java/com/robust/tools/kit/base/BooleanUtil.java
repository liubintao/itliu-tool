package com.robust.tools.kit.base;

import org.apache.commons.lang3.BooleanUtils;

/**
 * @Description: 1、从String(true/false,yes/no)，转换为Boolean或boolean
 * 2、逻辑运算：取反，多个boolean的and，or计算
 * 封装{@link org.apache.commons.lang3.BooleanUtils }
 * @Author: robust
 * @CreateDate: 2019/7/19 11:42
 * @Version: 1.0
 */
public class BooleanUtil {

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true", str为空时返回false
     *
     * @param str
     * @return
     */
    public static boolean toBoolean(String str) {
        return Boolean.getBoolean(str);
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true"，str为空时返回null
     *
     * @param str
     * @return
     */
    public static Boolean toBooleanObject(String str) {
        return str != null ? Boolean.valueOf(str) : null;
    }

    /**
     * 使用标准JDK，只分析是否忽略大小写的"true"，str为空时返回defaultValue
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static Boolean toBooleanObject(String str, Boolean defaultValue) {
        return str != null ? Boolean.valueOf(str) : defaultValue;
    }

    /**
     * 支持true/false, on/off, y/n, yes/no的转换, str为空或无法分析时返回null
     *
     * @param str
     * @return
     */
    public static Boolean parseGeneralString(String str) {
        return BooleanUtils.toBooleanObject(str);
    }

    /**
     * 支持true/false,on/off, y/n, yes/no的转换, str为空或无法分析时返回defaultValue
     */
    public static Boolean parseGeneralString(String str, Boolean defaultValue) {
        return BooleanUtils.toBooleanDefaultIfNull(BooleanUtils.toBooleanObject(str), defaultValue);
    }

    /**
     * 取反
     */
    public static boolean negate(final boolean bool) {
        return !bool;
    }

    /**
     * 取反
     */
    public static Boolean negate(final Boolean bool) {
        return BooleanUtils.negate(bool);
    }

    /**
     * 多个值的and
     */
    public static boolean and(final boolean... array) {
        return BooleanUtils.and(array);
    }

    /**
     * 多个值的or
     */
    public static boolean or(final boolean... array) {
        return BooleanUtils.or(array);
    }
}
