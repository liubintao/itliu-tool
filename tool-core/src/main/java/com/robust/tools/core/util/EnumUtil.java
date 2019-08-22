package com.robust.tools.core.util;

/**
 * @Description: 枚举工具类
 * @Author: robust
 * @CreateDate: 2019/7/18 17:07
 * @Version: 1.0
 */
public class EnumUtil {

    public static <E extends Enum<E>> E getEnum(final Class<E> eClass, final String code) {
        if(code == null) {
            return null;
        }
        try {
            return Enum.valueOf(eClass, code);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }
}
