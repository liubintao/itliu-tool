package com.robust.tools.kit.number;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/31 19:22
 * @Version: 1.0
 */
public class RandomUtilTest {

    @Test
    public void getRandom() {
        System.out.println(RandomUtil.threadLocalRandom().nextInt());
        System.out.println(RandomUtil.secureRandom().nextInt());
    }

    @Test
    public void randomNumber() {
        int n = RandomUtil.nextInt();
        System.out.println(n);
        assertThat(n).isBetween(0, Integer.MAX_VALUE);

        n = RandomUtil.nextInt(RandomUtil.threadLocalRandom());
        System.out.println(n);
        assertThat(n).isBetween(0, Integer.MAX_VALUE);

        n = RandomUtil.nextInt(RandomUtil.secureRandom());
        System.out.println(n);
        assertThat(n).isBetween(0, Integer.MAX_VALUE);

        n = RandomUtil.nextInt(15);
        System.out.println(n);
        assertThat(n).isBetween(0, 15);

        n = RandomUtil.nextInt(RandomUtil.threadLocalRandom(), 15);
        System.out.println(n);
        assertThat(n).isBetween(0, 15);

        n = RandomUtil.nextInt(RandomUtil.secureRandom(), 15);
        System.out.println(n);


        n = RandomUtil.nextInt(13, 15);
        System.out.println(n);
        assertThat(n).isBetween(13, 15);

        n = RandomUtil.nextInt(RandomUtil.threadLocalRandom(), 13, 15);
        System.out.println(n);
        assertThat(n).isBetween(13, 15);

        n = RandomUtil.nextInt(RandomUtil.secureRandom(), 13, 15);
        System.out.println(n);
        assertThat(n).isBetween(13, 15);

        Long l = RandomUtil.nextLong();
        System.out.println(l);
        assertThat(l).isBetween(0L, Long.MAX_VALUE);

        l = RandomUtil.nextLong(RandomUtil.threadLocalRandom());
        System.out.println(l);
        assertThat(l).isBetween(0L, Long.MAX_VALUE);

        l = RandomUtil.nextLong(RandomUtil.secureRandom());
        System.out.println(l);
        assertThat(l).isBetween(0L, Long.MAX_VALUE);

        l = RandomUtil.nextLong(10);
        System.out.println(l);
        assertThat(l).isBetween(0L, 10L);

        l = RandomUtil.nextLong(RandomUtil.threadLocalRandom(), 10);
        System.out.println(l);
        assertThat(l).isBetween(0L, 10L);

        l = RandomUtil.nextLong(RandomUtil.secureRandom(), 10);
        System.out.println(l);
        assertThat(l).isBetween(0L, 10L);

        l = RandomUtil.nextLong(5, 10);
        System.out.println(l);
        assertThat(l).isBetween(5L, 10L);

        l = RandomUtil.nextLong(RandomUtil.secureRandom(), 5, 10);
        System.out.println(l);
        assertThat(l).isBetween(5L, 10L);

        l = RandomUtil.nextLong(RandomUtil.threadLocalRandom(), 5, 10);
        System.out.println(l);
        assertThat(l).isBetween(5L, 10L);

        Double d = RandomUtil.nextDouble();
        System.out.println(d);
        assertThat(d).isBetween(0.0, Double.MAX_VALUE);

        d = RandomUtil.nextDouble(RandomUtil.threadLocalRandom());
        System.out.println(d);
        assertThat(d).isBetween(0.0, Double.MAX_VALUE);

        d = RandomUtil.nextDouble(RandomUtil.secureRandom());
        System.out.println(d);
        assertThat(d).isBetween(0.0, Double.MAX_VALUE);

        d = RandomUtil.nextDouble(10);
        System.out.println(d);
        assertThat(d).isBetween(0.0, 10D);

        d = RandomUtil.nextDouble(RandomUtil.threadLocalRandom(), 10);
        System.out.println(d);
        assertThat(d).isBetween(0.0, 10D);

        d = RandomUtil.nextDouble(RandomUtil.secureRandom(), 10);
        System.out.println(d);
        assertThat(d).isBetween(0.0, 10D);

        d = RandomUtil.nextDouble(5, 10);
        System.out.println(d);
        assertThat(d).isBetween(5D, 10D);

        d = RandomUtil.nextDouble(RandomUtil.threadLocalRandom(), 5, 10);
        System.out.println(d);
        assertThat(d).isBetween(5D, 10D);

        d = RandomUtil.nextDouble(RandomUtil.secureRandom(), 5, 10);
        System.out.println(d);
        assertThat(d).isBetween(5D, 10D);
    }

    @Test
    public void randomString() {

        System.out.println(RandomUtil.randomStringFixLength(5));
        System.out.println(RandomUtil.randomStringFixLength(RandomUtil.threadLocalRandom(), 5));
        System.out.println(RandomUtil.randomStringFixLength(RandomUtil.secureRandom(), 5));

        assertThat(RandomUtil.randomStringFixLength(5)).hasSize(5);
        assertThat(RandomUtil.randomStringFixLength(RandomUtil.threadLocalRandom(), 5)).hasSize(5);
        assertThat(RandomUtil.randomStringFixLength(RandomUtil.secureRandom(), 5)).hasSize(5);

        System.out.println(RandomUtil.randomStringRandomLength(5, 10));
        assertThat(RandomUtil.randomStringRandomLength(5, 10).length()).isBetween(5, 10);

        String s = RandomUtil.randomStringRandomLength(RandomUtil.threadLocalRandom(), 1, 5);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 5);

        s = RandomUtil.randomStringRandomLength(RandomUtil.secureRandom(), 1, 5);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 5);

        s = RandomUtil.randomLetterRandomLength(3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomLetterRandomLength(RandomUtil.threadLocalRandom(), 3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomLetterRandomLength(RandomUtil.secureRandom(), 3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomLetterRandomLength(1, 3);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 3);

        s = RandomUtil.randomLetterRandomLength(RandomUtil.threadLocalRandom(), 1, 3);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 3);

        s = RandomUtil.randomAsciiFixLength(3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomAsciiFixLength(RandomUtil.threadLocalRandom(), 3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomAsciiFixLength(RandomUtil.secureRandom(), 3);
        System.out.println(s);
        assertThat(s.length()).isEqualTo(3);

        s = RandomUtil.randomAsciiRandomLength(1, 3);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 3);

        s = RandomUtil.randomAsciiRandomLength(RandomUtil.threadLocalRandom(), 1, 10);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 10);

        s = RandomUtil.randomAsciiRandomLength(RandomUtil.secureRandom(), 1, 10);
        System.out.println(s);
        assertThat(s.length()).isBetween(1, 10);
    }
}