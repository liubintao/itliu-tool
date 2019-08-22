package com.robust.tools.kit.concurrent.limit;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/31 11:35
 * @Version: 1.0
 */
public class RateLimitUtilTest {
    @Test
    public void create() throws ReflectiveOperationException {
        RateLimiter rateLimiter = RateLimiterUtil.create(10, 2);

        Class superClass = rateLimiter.getClass().getSuperclass();
        Field field = superClass.getDeclaredField("storedPermits");
        field.setAccessible(true);

        Assert.assertEquals(20, (int) field.getDouble(rateLimiter));
    }
}