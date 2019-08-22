package com.robust.tools.kit.time;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: DateFormat.format()消耗较大，如果时间戳是递增的，而且同一单位内有多次format()，使用用本类减少重复调用.
 * <p>
 * copy from Log4j2 DatePatternConverter，进行了优化，根据输出格式是否毫秒级，决定缓存在秒级还是毫秒级.
 * <p>
 * 注意如果输出格式为毫秒级的话，根据QPS决定性价比
 * <p>
 * see https://github.com/apache/logging-log4j2/blob/master/log4j-core/src/main/java/org/apache/logging/log4j/core/pattern/DatePatternConverter.java#L272
 * @Author: robust
 * @CreateDate: 2019/8/22 14:17
 * @Version: 1.0
 */
public class CachingTimeFormatter {

    private FastDateFormat fastDateFormat;
    private AtomicReference<CachedTime> cachedTime;
    //根据时间格式, 决定缓存在秒级还是毫秒级
    private boolean onSecond;

    public CachingTimeFormatter(FastDateFormat fastDateFormat) {
        this.fastDateFormat = fastDateFormat;
        this.onSecond = fastDateFormat.getPattern().indexOf("SSS") == -1;

        long current = System.currentTimeMillis();
        this.cachedTime = new AtomicReference<>(new CachedTime(current, fastDateFormat.format(current)));
    }

    public CachingTimeFormatter(String pattern) {
        this(FastDateFormat.getInstance(pattern));
    }

    public String format(final long timestampMillis) {
        CachedTime cachedTime = this.cachedTime.get();

        long timestamp = this.onSecond ? timestampMillis / 1000 : timestampMillis;

        if (timestamp != cachedTime.timestamp) {
            final CachedTime newCachedTime = new CachedTime(timestamp, this.fastDateFormat.format(timestampMillis));
            //尝试放入CachedTime
            this.cachedTime.compareAndSet(cachedTime, newCachedTime);
            // 与log4j2做法不同，无论是否放入成功，都使用自己的值
            cachedTime = newCachedTime;
        }
        return cachedTime.formatted;
    }

    private static final class CachedTime {
        long timestamp;
        String formatted;

        public CachedTime(long timestamp, String formatted) {
            this.timestamp = timestamp;
            this.formatted = formatted;
        }
    }
}
