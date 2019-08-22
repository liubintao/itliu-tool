package com.robust.tools.kit.base;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/7/30 17:14
 * @Version: 1.0
 */
public class ValueValidatorTest {

    @Test
    public void checkAndGet() {

        assertThat(ValueValidator.checkAndGet(0, 1, ValueValidator.Validator.INTEGER_GT_ZERO_VALIDATOR)).isEqualTo(1);
        assertThat(ValueValidator.checkAndGet("", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("a");
        assertThat(ValueValidator.checkAndGet("     ", "a", ValueValidator.Validator.STRING_NOT_BLANK_VALIDATOR)).isEqualTo("a");
        assertThat(ValueValidator.checkAndGet("true", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("true");
        assertThat(ValueValidator.checkAndGet("false", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("false");
        assertThat(ValueValidator.checkAndGet("yes", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("yes");
        assertThat(ValueValidator.checkAndGet("no", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("no");
        assertThat(ValueValidator.checkAndGet("on", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("on");
        assertThat(ValueValidator.checkAndGet("off", "a", ValueValidator.Validator.STRING_NOT_EMPTY_VALIDATOR)).isEqualTo("off");
    }
}