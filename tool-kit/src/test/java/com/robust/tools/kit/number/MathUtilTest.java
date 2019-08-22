package com.robust.tools.kit.number;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/26 16:03
 * @Version: 1.0
 */
public class MathUtilTest {

    @Test
    public void safeFindNextPositivePowerOfTwo() {
        assertThat(MathUtil.safeFindNextPositivePowerOfTwo(2)).isEqualTo(2);
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(2));
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(3));
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(4));
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(5));
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(6));
        System.out.println(MathUtil.safeFindNextPositivePowerOfTwo(15));
    }

    @Test
    public void findNextPositivePowerOfTwo() {
    }
}