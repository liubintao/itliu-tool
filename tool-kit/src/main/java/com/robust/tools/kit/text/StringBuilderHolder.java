package com.robust.tools.kit.text;

/**
 * @Description: 参考Netty的InternalThreadLocalMap 与 BigDecimal, 放在threadLocal中重用的StringBuilder, 节约StringBuilder内部的char[]
 * <p>
 * 参考文章：《StringBuilder在高性能场景下的正确用法》http://calvin1978.blogcn.com/articles/stringbuilder.html
 * <p>
 * 不过仅在String对象较大时才有明显效果，否则抵不上访问ThreadLocal的消耗.
 * <p>
 * 当StringBuilder在使用过程中，会调用其他可能也使用StringBuilderHolder的子函数时，需要创建独立的Holder, 否则会共同使用公共的Holder
 * <p>
 * 注意：在Netty环境中，使用Netty提供的基于FastThreadLocal的版本
 * @Author: robust
 * @CreateDate: 2019/8/22 12:56
 * @Version: 1.0
 */
public class StringBuilderHolder {
    private int initSize;

    //全局公共的ThreadLocal StringBuilder
    private static ThreadLocal<StringBuilder> globalStringBuilder = ThreadLocal.withInitial(() -> {
        return new StringBuilder(512);
    });

    //独立创建的ThreadLocal的StringBuilder
    private ThreadLocal<StringBuilder> stringBuilder = ThreadLocal.withInitial(() -> {
        return new StringBuilder(initSize);
    });

    /**
     * 创建独立的Holder.
     * <p>
     * 用于StringBuilder在使用过程中，会调用其他可能也使用StringBuilderHolder的子函数.
     *
     * @param initSize StringBuilder的初始大小, 建议512,如果容量不足将进行扩容，扩容后的数组将一直保留.
     */
    public StringBuilderHolder(int initSize) {
        this.initSize = initSize;
    }

    /**
     * 获取公共Holder的StringBuilder.
     * <p>
     * 当StringBuilder会被连续使用，期间不会调用其他可能也使用StringBuilderHolder的子函数时使用.
     * <p>
     * 重置StringBuilder内部的writerIndex, 而char[]保留不动.
     */
    public static StringBuilder getGlobal() {
        StringBuilder sb = globalStringBuilder.get();
        sb.setLength(0);
        return sb;
    }

    /**
     * 获取独立Holder的StringBuilder.
     * <p>
     * 重置StringBuilder内部的writerIndex, 而char[]保留不动.
     */
    public StringBuilder get() {
        StringBuilder sb = stringBuilder.get();
        sb.setLength(0);
        return sb;
    }
}
