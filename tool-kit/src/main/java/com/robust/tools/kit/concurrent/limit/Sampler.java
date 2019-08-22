package com.robust.tools.kit.concurrent.limit;

import com.robust.tools.kit.base.Validate;
import com.robust.tools.kit.number.RandomUtil;

/**
 * @Description: 采样器(美[sæmplɚ])
 * 移植 Twitter Common, 优化使用ThreadLocalRandom
 * https://github.com/twitter/commons/blob/master/src/java/com/twitter/common/util/Sampler.java
 * @Author: robust
 * @CreateDate: 2019/7/31 14:58
 * @Version: 1.0
 */
public class Sampler {

    //总是命中
    private static final Double ALWAYS = Double.valueOf(100);
    //从不命中
    private static final Double NEVER = Double.valueOf(0);
    //采样率阀值，超过此值进行采样
    private double threshold;

    protected Sampler() {
    }

    /**
     * @param selectPercent 采样率，在0-100之间，可以有小数位
     */
    protected Sampler(double selectPercent) {
        Validate.isTrue(selectPercent >= 0 && selectPercent <= 100, "Invalid selectPercent value : " + selectPercent);
        this.threshold = selectPercent / 100;
    }

    /**
     * 判断当前请求是否命中采样.
     *
     * @return
     */
    public boolean select() {
        return RandomUtil.threadLocalRandom().nextDouble() < threshold;
    }

    /**
     * 优化后的创建函数，如果为0或100时，返回更直接的采样器
     *
     * @param selectPercent
     * @return
     */
    public static Sampler create(Double selectPercent) {
        if (selectPercent.equals(ALWAYS)) {
            return AlwaysSampler.getInstance();
        } else if (selectPercent.equals(NEVER)) {
            return NeverSampler.getInstance();
        }
        return new Sampler(selectPercent);
    }

    /**
     * 采样率为100时，总是返回true
     */
    public static class AlwaysSampler extends Sampler {

        public static final AlwaysSampler INSTANCE = new AlwaysSampler();

        @Override
        public boolean select() {
            return true;
        }

        public static AlwaysSampler getInstance() {
            return INSTANCE;
        }
    }

    /**
     * 采样率为0时，总是返回true
     */
    public static class NeverSampler extends Sampler {
        public static final NeverSampler INSTANCE = new NeverSampler();

        @Override
        public boolean select() {
            return false;
        }

        public static NeverSampler getInstance() {
            return INSTANCE;
        }
    }
}
