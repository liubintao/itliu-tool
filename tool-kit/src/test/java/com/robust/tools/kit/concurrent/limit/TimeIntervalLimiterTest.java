package com.robust.tools.kit.concurrent.limit;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/1 10:34
 * @Version: 1.0
 */
public class TimeIntervalLimiterTest {

    @Test
    public void tryAcquire() throws InterruptedException {
        TimeIntervalLimiter limiter = new TimeIntervalLimiter(1000, TimeUnit.MILLISECONDS);

        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isFalse();

        TimeUnit.MILLISECONDS.sleep(1000);

        assertThat(limiter.tryAcquire()).isTrue();
        assertThat(limiter.tryAcquire()).isFalse();
    }
}