package com.robust.tools.kit.base;

import com.robust.tools.kit.text.StringUtil;

/**
 * @Description: 数值校验取值器
 * 提供对配置值进行校验，并根据结果决定是否使用默认值.
 * Guava,Commons Lang里的Validate类用于判断并抛出异常
 * 而ValueValidator的行为是取默认值，多用于配置值的处理.
 * 除默认提供的Validator，可自行扩写.
 * @Author: robust
 * @CreateDate: 2019/7/30 16:10
 * @Version: 1.0
 */
public class ValueValidator {

    /**
     * 对目标值进行校验，并根据校验结果取值
     * e.g
     * 校验目标值是否大于0,是则取原值,否则取默认值1
     * ValueValidator.checkAndGet(0, 1, Validator.INTEGER_GT_ZERO_VALIDATOR)
     *
     * @param value
     * @param defaultValue
     * @param validator
     * @param <T>
     * @return
     */
    public static <T> T checkAndGet(T value, T defaultValue, Validator<T> validator) {
        return validator.validate(value) ? value : defaultValue;
    }

    /**
     * Value校验器
     *
     * @param <T>
     */
    public interface Validator<T> {
        //校验值是否匹配
        boolean validate(T value);

        /**
         * 数值配置不为null且>0
         */
        Validator<Integer> INTEGER_GT_ZERO_VALIDATOR = (value) -> value != null && value > 0;
        /**
         * 字符串不为null且长度大于0
         */
        Validator<String> STRING_NOT_EMPTY_VALIDATOR = (s) -> StringUtil.isNotEmpty(s);
        /**
         * 字符串去掉空格后不为null且长度大于0
         */
        Validator<String> STRING_NOT_BLANK_VALIDATOR = (s) -> StringUtil.isNotBlank(s);
        /**
         * boolean字符串校验
         */
        Validator<String> STRING_BOOLEAN_VALIDATOR = (s) -> BooleanUtil.parseGeneralString(s)
                || !BooleanUtil.parseGeneralString(s);
    }
}
