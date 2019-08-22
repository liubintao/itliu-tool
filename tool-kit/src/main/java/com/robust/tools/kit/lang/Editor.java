package com.robust.tools.kit.lang;

/**
 * @Description: 编辑器接口，常用于对集合中的元素做统一编辑<br>
 *               此编辑器两个作用：
 *
 * <pre>
 * 1、如果返回值为<code>null</code>, 表示此值被抛弃
 * 2、对对象做修改
 * </pre>
 *
 * @Author: robust
 * @CreateDate: 2019/7/16 16:23
 * @Version: 1.0
 */
@FunctionalInterface
public interface Editor<T> {

    /**
     * 修改过滤后的结果
     * @param t 被过滤的对象
     * @return 修改后的对象
     */
    T edit(T t);
}
