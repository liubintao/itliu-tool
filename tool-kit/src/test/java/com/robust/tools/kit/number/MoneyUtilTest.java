package com.robust.tools.kit.number;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/20 17:01
 * @Version: 1.0
 */
public class MoneyUtilTest {

    @Test
    public void fen2yuan() {
        BigDecimal b = new BigDecimal(100);
        assertThat(MoneyUtil.fen2yuan(b).doubleValue()).isEqualTo(1);

        assertThat(MoneyUtil.fen2yuan(1000L).doubleValue()).isEqualTo(10);

        assertThat(MoneyUtil.fen2yuan("2000").doubleValue()).isEqualTo(20);
    }

    @Test
    public void yuan2fen() {
        BigDecimal b = new BigDecimal(1);
        assertThat(MoneyUtil.yuan2fen(b).doubleValue()).isEqualTo(100);

        assertThat(MoneyUtil.yuan2fen(20d).doubleValue()).isEqualTo(2000);

        assertThat(MoneyUtil.yuan2fen("30").doubleValue()).isEqualTo(3000);
    }

    @Test
    public void format() {
        BigDecimal b = BigDecimal.TEN;
        assertThat(MoneyUtil.format(b)).isEqualTo("10.00");

        assertThat(MoneyUtil.format(10d)).isEqualTo("10.00");
    }

    @Test
    public void prettyFormat() {
        BigDecimal b = new BigDecimal(10000);
        assertThat(MoneyUtil.prettyFormat(b)).isEqualTo("10,000.00");

        assertThat(MoneyUtil.prettyFormat(10000d)).isEqualTo("10,000.00");
    }

    @Test
    public void format2String() {
        BigDecimal b = new BigDecimal(10000);
        assertThat(MoneyUtil.format(b, "0.00")).isEqualTo("10000.00");

        assertThat(MoneyUtil.format(10000d, "0.00")).isEqualTo("10000.00");
    }

    @Test
    public void parseString() throws ParseException {
        assertThat(MoneyUtil.parseString("10.00").doubleValue()).isEqualTo(10.00);
        assertThat(MoneyUtil.parsePrettyString("10,000.00").doubleValue()).isEqualTo(10000.00);

        assertThat(MoneyUtil.parseString("10,000.00", "#,##0.00").doubleValue()).isEqualTo(10000.00);
        assertThat(MoneyUtil.parseString("10000.00", "0.00").doubleValue()).isEqualTo(10000.00);
    }

    @Test
    public void parsePrettyString() {
    }

    @Test
    public void parseString1() {
    }
}