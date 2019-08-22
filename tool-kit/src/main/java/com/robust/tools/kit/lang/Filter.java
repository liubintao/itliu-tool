package com.robust.tools.kit.lang;

/**
 * @Description: 过滤器接口
 * @Author: robust
 * @CreateDate: 2019/7/16 20:57
 * @Version: 1.0
 */
@FunctionalInterface
public interface Filter<T> {
    /**
     * 是否接受此对象
     * @param t 被过滤对象
     * @return true or false
     */
    boolean accept(T t);
}
