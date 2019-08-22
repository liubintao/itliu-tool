package com.robust.tools.kit.number;

import com.robust.tools.kit.base.Validate;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: 随机数工具集.
 * 1、获取无锁的ThreadLocalRandom,性能较佳的SecureRandom
 * 2、保证没有负数陷阱，也能更精确设定范围的nextInt/nextLong/nextDouble
 * copy from {@link org.apache.commons.lang3.RandomUtils}, 但默认使用性能较优的ThreadLocalRandom, 并可配置其他的Random
 * 3. 随机字符串 {@link org.apache.commons.lang3.RandomStringUtils}
 * @Author: robust
 * @CreateDate: 2019/7/31 15:28
 * @Version: 1.0
 */
public class RandomUtil {

    /**
     * 返回无锁的ThreadLocalRandom
     *
     * @return
     * @see ThreadLocalRandom
     */
    public static Random threadLocalRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * SecureRandom使用性能更好的SHA1PRNG, TOMCAT的sessionId生成也用此算法.
     * 但JDK7中,需要在启动参数中加入 -Djava.security=file:/dev/./urandom(中间那个点很重要)
     * 详见：《SecureRandom的江湖偏方与真实效果》http://calvin1978.blogcn.com/articles/securerandom.html
     *
     * @return
     */
    public static SecureRandom secureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            return new SecureRandom();
        }
    }

    /*--------------------nextInt相关--------------------------------*/

    /**
     * 返回0到{@link Integer#MAX_VALUE}的随机int,使用ThreadLocalRandom
     *
     * @return
     */
    public static int nextInt() {
        return nextInt(ThreadLocalRandom.current());
    }

    /**
     * 返回0到{@link Integer#MAX_VALUE}的随机int,可传入ThreadLocalRandom或SecureRandom
     *
     * @param random
     * @return
     */
    public static int nextInt(final Random random) {
        int n = random.nextInt();
        if (n == Integer.MAX_VALUE) {
            return 0;
        } else {
            n = Math.abs(n);
        }
        return n;
    }

    /**
     * 返回0到{@link Integer#MAX_VALUE}的随机int,使用ThreadLocalRandom
     *
     * @param max
     * @return
     */
    public static int nextInt(final int max) {
        return nextInt(ThreadLocalRandom.current(), max);
    }

    /**
     * 返回0到max的随机int,可传入ThreadLocalRandom或SecureRandom
     *
     * @param random
     * @param max
     * @return
     */
    public static int nextInt(final Random random, final int max) {
        return random.nextInt(max);
    }

    /**
     * 返回min到max的随机int,使用ThreadLocalRandom
     *
     * @param min 必须大于0
     * @param max
     * @return
     */
    public static int nextInt(final int min, final int max) {
        return nextInt(ThreadLocalRandom.current(), min, max);
    }

    /**
     * 返回min到max的随机int
     * JDK本身不具有控制两端范围的nextInt，因此参考Commons Lang RandomUtils的实现, 不直接复用是因为要传入Random实例
     *
     * @param random 可传入ThreadLocalRandom或SecureRandom
     * @param min    必须大于0
     * @param max
     * @return
     * @see org.apache.commons.lang3.RandomUtils#nextInt(int, int)
     */
    public static int nextInt(final Random random, final int min, final int max) {
        Validate.isTrue(max >= min, "Start value must by smaller or equal to end value.");
        Validate.nonNegative("min", min);
        if (min == max) {
            return min;
        }
        return min + random.nextInt(max - min);
    }

    /*--------------------nextLong相关--------------------------------*/

    /**
     * 返回0至{@link Long#MAX_VALUE}间的随机long,使用ThreadLocalRandom
     *
     * @return
     */
    public static long nextLong() {
        return nextLong(ThreadLocalRandom.current());
    }

    /**
     * 返回0至{@link Long#MAX_VALUE}间的随机long
     *
     * @param random 可传入ThreadLocalRandom或SecureRandom
     * @return
     */
    public static long nextLong(final Random random) {
        long n = random.nextLong();
        if (n == Long.MAX_VALUE) {
            return 0;
        } else {
            n = Math.abs(n);
        }
        return n;
    }

    /**
     * 返回0至max间的随机long,使用ThreadLocalRandom
     *
     * @param max
     * @return
     */
    public static long nextLong(final long max) {
        return nextLong(ThreadLocalRandom.current(), 0, max);
    }

    /**
     * 返回0至max间的随机long
     *
     * @param random 可传入ThreadLocalRandom或SecureRandom
     * @param max
     * @return
     */
    public static long nextLong(final Random random, final long max) {
        return nextLong(random, 0, max);
    }

    /**
     * 返回0至max间的随机long,使用ThreadLocalRandom
     *
     * @param min 大于0
     * @param max
     * @return
     */
    public static long nextLong(final long min, final long max) {
        return nextLong(ThreadLocalRandom.current(), min, max);
    }

    /**
     * 返回0至max间的随机long
     * <p>
     * JDK本身不具有控制两端范围的nextLong，因此参考Commons Lang RandomUtils的实现, 不直接复用是因为要传入Random实例
     *
     * @param random 可传入ThreadLocalRandom或SecureRandom
     * @param min    必须大于0
     * @param max
     * @return
     * @see org.apache.commons.lang3.RandomUtils#nextLong(long, long)
     */
    public static long nextLong(final Random random, final long min, final long max) {
        Validate.isTrue(max >= min, "Start value must smaller or equal end value.");
        Validate.nonNegative("min", min);
        if (min == max) {
            return min;
        }
        return (long) (min + (max - min) * random.nextDouble());
    }

    /*--------------------nextDouble相关--------------------------------*/

    /**
     * 返回0-{@link Double#MAX_VALUE}之间的double, 使用ThreadLocalRandom
     */
    public static double nextDouble() {
        return nextDouble(ThreadLocalRandom.current(), 0, Double.MAX_VALUE);
    }

    /**
     * 返回0-{@link Double#MAX_VALUE}之间的double
     */
    public static double nextDouble(final Random random) {
        return nextDouble(random, 0, Double.MAX_VALUE);
    }

    /**
     * 返回0-max之间的double, 使用ThreadLocalRandom
     * <p>
     * 注意：与JDK默认返回0-1的行为不一致.
     */
    public static double nextDouble(final double max) {
        return nextDouble(ThreadLocalRandom.current(), 0, max);
    }

    /**
     * 返回0-max之间的double
     */
    public static double nextDouble(final Random random, final double max) {
        return nextDouble(random, 0, max);
    }

    /**
     * 返回min-max之间的double,ThreadLocalRandom
     */
    public static double nextDouble(final double min, final double max) {
        return nextDouble(ThreadLocalRandom.current(), min, max);
    }

    /**
     * 返回min-max之间的double
     */
    public static double nextDouble(final Random random, final double min, final double max) {
        Validate.isTrue(max >= min, "Start value must be smaller or equal to end value.");
        Validate.nonNegative("min", min);

        if (Double.compare(min, max) == 0) {
            return min;
        }

        return min + ((max - min) * random.nextDouble());
    }

    /*----------------------String-------------------------------------*/

    /**
     * 随机字母或数字
     *
     * @param length 固定长度
     * @return
     */
    public static String randomStringFixLength(final int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, threadLocalRandom());
    }

    /**
     * 随机字母或数字
     *
     * @param length 固定长度
     * @return
     */
    public static String randomStringFixLength(final Random random, final int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, random);
    }

    /**
     * 随机字母或数字, 随机长度
     *
     * @param min
     * @param max
     * @return
     */
    public static String randomStringRandomLength(final int min, final int max) {
        return RandomStringUtils.random(nextInt(min, max), 0, 0, true, true, null, threadLocalRandom());
    }

    /**
     * 随机字母或数字, 随机长度
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static String randomStringRandomLength(final Random random, final int min, final int max) {
        return RandomStringUtils.random(nextInt(min, max), 0, 0, true, true, null, random);
    }

    /**
     * 随机字母,固定长度
     *
     * @param length
     * @return
     */
    public static String randomLetterRandomLength(final int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null, threadLocalRandom());
    }

    /**
     * 随机字母,固定长度
     *
     * @param random
     * @param length
     * @return
     */
    public static String randomLetterRandomLength(final Random random, final int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null, random);
    }

    /**
     * 随机字母,随机长度
     *
     * @param min
     * @param max
     * @return
     */
    public static String randomLetterRandomLength(final int min, final int max) {
        return RandomStringUtils.random(nextInt(min, max), 0, 0, true, false, null, threadLocalRandom());
    }

    /**
     * 随机字母,随机长度
     *
     * @param random
     * @param min
     * @param max
     * @return
     */
    public static String randomLetterRandomLength(final Random random, final int min, final int max) {
        return RandomStringUtils.random(nextInt(min, max), 0, 0, true, false, null, random);
    }

    /**
     * 随机ASCII字符(含字母，数字及其他符号),固定长度
     *
     * @param length
     * @return
     */
    public static String randomAsciiFixLength(final int length) {
        return RandomStringUtils.random(length, 32, 127, false, false, null, threadLocalRandom());
    }

    /**
     * 随机ASCII字符(含字母，数字及其他符号),固定长度
     *
     * @param random
     * @param length
     * @return
     */
    public static String randomAsciiFixLength(final Random random, final int length) {
        return RandomStringUtils.random(length, 32, 127, false, false, null, random);
    }

    /**
     * 随机ASCII字符(含字母，数字及其他符号),随机长度
     *
     * @param minLength
     * @param maxLength
     * @return
     */
    public static String randomAsciiRandomLength(final int minLength, final int maxLength) {
        return RandomStringUtils.random(nextInt(minLength, maxLength), 32, 127, false, false, null,
                threadLocalRandom());
    }

    /**
     * 随机ASCII字符(含字母，数字及其他符号)，随机长度
     *
     * @param random
     * @param minLength
     * @param maxLength
     * @return
     */
    public static String randomAsciiRandomLength(final Random random, final int minLength, final int maxLength) {
        return RandomStringUtils.random(nextInt(random, minLength, maxLength), 32, 127, false, false, null, random);
    }
}
