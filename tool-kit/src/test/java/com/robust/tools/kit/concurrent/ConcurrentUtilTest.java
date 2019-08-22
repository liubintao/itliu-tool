package com.robust.tools.kit.concurrent;

import org.junit.Test;

import java.util.concurrent.atomic.LongAdder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/6 10:50
 * @Version: 1.0
 */
public class ConcurrentUtilTest {

    @Test
    public void longAdder() {
        LongAdder longAdder = ConcurrentUtil.longAdder();
        longAdder.increment();
        longAdder.add(3);
        assertThat(longAdder.sum()).isEqualTo(4);
    }
}