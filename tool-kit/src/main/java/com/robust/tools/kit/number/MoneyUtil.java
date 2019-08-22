package com.robust.tools.kit.number;

import com.robust.tools.kit.text.StringUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @Description: 货币工具类
 * <p>
 * 1、元和分之间的转换
 * 2、货币格式化成字符串
 * 3、字符串转换成货币
 * @Author: robust
 * @CreateDate: 2019/8/20 13:00
 * @Version: 1.0
 */
public class MoneyUtil {

    private static final ThreadLocal<DecimalFormat> DEFAULT_FORMAT = createThreadLocalNumberFormat("0.00");
    private static final ThreadLocal<DecimalFormat> PRETTY_FORMAT = createThreadLocalNumberFormat("#,##0.00");

    /**
     * ThreadLocal重用MessageDigest
     *
     * @param pattern
     * @return
     */
    private static ThreadLocal<DecimalFormat> createThreadLocalNumberFormat(final String pattern) {
        return ThreadLocal.withInitial(() -> {
            DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
            decimalFormat.applyPattern(pattern);
            return decimalFormat;
        });
    }

    /*--------------------元和分的转换------------------------------*/

    /**
     * 人民币金额单位转换,分转换成元,保留两位小数 例: 100 -> 1.00
     */
    public static BigDecimal fen2yuan(BigDecimal num) {
        return num == null ? BigDecimal.ZERO : num.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * 人民币金额单位转换,分转换成元,保留两位小数 例: 100 -> 1.00
     */
    public static BigDecimal fen2yuan(long num) {
        return fen2yuan(new BigDecimal(num));
    }

    /**
     * 人民币金额单位转换,分转换成元,保留两位小数 例: 100 -> 1.00
     */
    public static BigDecimal fen2yuan(String num) {
        return StringUtil.isBlank(num) ? BigDecimal.ZERO : fen2yuan(new BigDecimal(num));
    }

    /**
     * 人民币金额单位转换,元转换成分, 例如:1 => 100
     */
    public static BigDecimal yuan2fen(String yuan) {
        return StringUtil.isBlank(yuan) ? BigDecimal.ZERO : new BigDecimal(yuan).multiply(new BigDecimal(100));
    }

    /**
     * 人民币金额单位转换,元转换成分, 例如:1 => 100
     */
    public static BigDecimal yuan2fen(double yuan) {
        return BigDecimal.valueOf(yuan).multiply(new BigDecimal(100));
    }

    /**
     * 人民币金额单位转换,元转换成分, 例如:1 => 100
     */
    public static BigDecimal yuan2fen(BigDecimal yuan) {
        return yuan == null ? BigDecimal.ZERO : yuan.multiply(new BigDecimal(100));
    }

    /*----------------格式化输出-----------------*/

    /**
     * 格式化金额, 例: 1 -> 1.00
     */
    public static String format(BigDecimal number) {
        return format(number.doubleValue());
    }

    /**
     * 格式化金额, 例: 1 -> 1.00
     */
    public static String format(double number) {
        return DEFAULT_FORMAT.get().format(number);
    }

    /**
     * 格式化金额，默认格式：#,##0.00 ,例如：10000.00 输出：10,000.00
     */
    public static String prettyFormat(BigDecimal number) {
        return prettyFormat(number.doubleValue());
    }

    /**
     * 格式化金额，默认格式：#,##0.00 ,例如：10000.00 输出：10,000.00
     */
    public static String prettyFormat(double number) {
        return PRETTY_FORMAT.get().format(number);
    }

    /**
     * 格式化金额,当pattern为空时,pattern默认为#,##0.00
     */
    public static String format(BigDecimal number, String pattern) {
        return format(number.doubleValue(), pattern);
    }


    /**
     * 格式化金额,当pattern为空时,pattern默认为#,##0.00
     */
    public static String format(double number, String pattern) {
        DecimalFormat df;
        if (StringUtil.isBlank(pattern)) {
            df = PRETTY_FORMAT.get();
        } else {
            df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern(pattern);
        }
        return df.format(number);
    }

    /*-----------------转换金额字符串为金额-----------------------*/

    /**
     * 按格式分析字符串 默认为0.00
     */
    public static BigDecimal parseString(String number) throws ParseException {
        return new BigDecimal(DEFAULT_FORMAT.get().parse(number).doubleValue());
    }

    /**
     * 按格式分析字符串 默认为#,##0.00
     */
    public static BigDecimal parsePrettyString(String number) throws ParseException {
        return parseString(number, null);
    }

    /**
     * 按格式分析字符串，当pattern为空时，pattern默认为#,##0.00
     */
    public static BigDecimal parseString(String number, String pattern) throws ParseException {
        DecimalFormat df;
        if (StringUtil.isBlank(pattern)) {
            df = PRETTY_FORMAT.get();
        } else {
            df = (DecimalFormat) DecimalFormat.getInstance();
            df.applyPattern(pattern);
        }

        return new BigDecimal(df.parse(number).doubleValue());
    }
}
